"use client";
import { useState, createContext, useContext, useEffect, useRef } from "react";
import { useUser } from "./UserProvider";
import { fetchChats } from "../services/api";
import { Client } from "@stomp/stompjs";

const ChatContext = createContext();

export const useChat = () => {
  const context = useContext(ChatContext);
  if (!context) {
    throw new Error("useChat must be used within a ChatProvider");
  }

  return context;
};

export const ChatProvider = ({ children }) => {
  const { user, isLoadingUser } = useUser();
  const [chats, setChats] = useState([]);
  const [isLoadingChats, setIsLoadingChats] = useState(true);
  const stompClient = useRef(null);

  useEffect(() => {
    if (!user || isLoadingUser) {
      return;
    }

    setIsLoadingChats(true);
    fetchData();
    setIsLoadingChats(false);
  }, [user]);

  const fetchData = async () => {
    const data = await fetchChats(user);
    setChats(data);
    connectSockets(data);
  };

  const connectSockets = (data) => {
    const client = new Client({
      brokerURL: process.env.NEXT_PUBLIC_API_WEBSOCKET_URL,
      connectHeaders: {
        Authorization: `Bearer ${user.token}`,
      },
    });

    client.onConnect = () => {
      data.forEach((chat) => {
        client.subscribe(`/chat/${chat.id}`, (message) => {
          const parsedMessage = JSON.parse(message.body);
          setChats((prevChats) =>
            prevChats.map((chat) =>
              chat.id === parsedMessage.chat_id
                ? { ...chat, last_message: parsedMessage }
                : chat
            )
          );
        });
      });
    };

    stompClient.current = client;
    client.activate();
  };

  return (
    <ChatContext.Provider value={{ chats, isLoadingChats }}>
      {children}
    </ChatContext.Provider>
  );
};
