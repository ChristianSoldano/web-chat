"use client";
import { useEffect, useRef, useState } from "react";
import OpenChatItem from "./OpenChatMessage";
import Image from "next/image";
import sendMessageIcon from "@/../public/send-button.svg";
import Spinner from "../Spinner";
import { useUser } from "@/app/context/UserProvider";
import { fetchMessages, sendMessage } from "@/app/services/api";
import { useChat } from "@/app/context/ChatProvider";

export default ({ selectedChat }) => {
  const { user, isLoadingUser } = useUser();
  const messagesRef = useRef(null);
  const [messages, setMessages] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [lastMessageId, setLastMessageId] = useState(null);
  const [canFetch, setCanFetch] = useState(true);
  const [messageText, setMessageText] = useState("");
  const { chats } = useChat();

  useEffect(() => {
    fetchData();
  }, [selectedChat]);

  useEffect(() => {
    if (!user || isLoadingUser || !selectedChat) {
      return;
    }

    const openChat = chats.find((item) => item.id === selectedChat.id);
    setMessages((prevMessages) => {
      if (prevMessages[0].id === openChat?.last_message.id) {
        return prevMessages;
      }
      
      return [openChat?.last_message, ...prevMessages];
    });
  }, [chats]);

  useEffect(() => {
    if (messages.length <= 20) {
      scrollToIndex(0);
    }
  }, [messages]);

  const fetchData = async () => {
    if (!user || isLoadingUser || !selectedChat) {
      return;
    }

    setIsLoading(true);
    const response = await fetchMessages(user, selectedChat.id);
    setMessages(response);
    setLastMessageId(getLastMessageId(response));
    setIsLoading(false);
  };

  const fetchPreviousMessages = async () => {
    if (
      !user ||
      isLoadingUser ||
      !selectedChat ||
      !canFetch ||
      !lastMessageId
    ) {
      return;
    }

    setCanFetch(false);
    const response = await fetchMessages(user, selectedChat.id, lastMessageId);
    setMessages((prevMessages) => [...prevMessages, ...response]);
    setLastMessageId(getLastMessageId(response));
    setCanFetch(true);
  };

  const getLastMessageId = (response) => {
    return response.length === 20 ? response[response.length - 1].id : null;
  };

  const scrollToIndex = (index) => {
    if (messagesRef.current && messagesRef.current.children.length > index) {
      messagesRef.current.children[index].scrollIntoView();
    }
  };

  const handleScroll = (e) => {
    if (e.target.scrollTop > 10 || !lastMessageId || isLoading) {
      return;
    }

    const lastElementChild = messagesRef.current?.lastElementChild;
    const lastElementChildIndex = Array.from(
      messagesRef.current.children
    ).indexOf(lastElementChild);
    fetchPreviousMessages();
    scrollToIndex(lastElementChildIndex);
  };

  const handleSendMessage = () => {
    if (!messageText) {
      return;
    }

    const message = {
      type: "TEXT",
      content: messageText,
    };
    sendMessage(user, selectedChat.id, message);
    setMessageText("");
    scrollToIndex(0);
  };

  const handleKeyPress = (e) => {
    if (e.key === "Enter") {
      e.preventDefault();
      handleSendMessage();
    }
  };

  return (
    <div className="open-chat">
      <div className="contact-header">{selectedChat?.chat_with}</div>
      {!selectedChat ? (
        <h1>SELECCIONA UN CHAT PERRO</h1>
      ) : (
        <>
          <div className="chat" onScroll={handleScroll}>
            {isLoading ? (
              <Spinner />
            ) : messages.length > 0 ? (
              <ul ref={messagesRef}>
                {messages.map((message, i) => {
                  return <OpenChatItem message={message} user={user} key={i} />;
                })}
              </ul>
            ) : null}
          </div>
          <div className="send">
            <input
              type="text"
              placeholder="Type a message"
              value={messageText}
              onChange={(e) => setMessageText(e.target.value)}
              onKeyDown={(e) => handleKeyPress(e)}
            />
            <button onClick={handleSendMessage}>
              <Image
                src={sendMessageIcon}
                width={40}
                height={40}
                alt="send button"
                className="send-button"
              />
            </button>
          </div>{" "}
        </>
      )}
    </div>
  );
};
