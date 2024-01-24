"use client";
import ChatHistory from "@/app/components/chat-history/ChatHistory";
import OpenChat from "@/app/components/open-chat/OpenChat";
import { useEffect, useRef, useState } from "react";
import { useRouter } from "next/navigation";
import { useUser } from "../context/UserProvider";
import { fetchChats } from "../services/api";
import Spinner from "../components/Spinner";
import logoutButton from "@/../public/logout.svg";
import Image from "next/image";

export default function Chat() {
  const router = useRouter();
  const { user, logout, isLoadingUser } = useUser();
  const [chats, setChats] = useState([]);
  const [selectedChat, setSelectedChat] = useState("");
  const [isLoading, setIsLoading] = useState(true);  

  const fetchData = async () => {
    if (!user || isLoadingUser) {
      return;
    }

    setIsLoading(true);
    const data = await fetchChats(user);
    setChats(data);
    setSelectedChat(data[0]);    
    setIsLoading(false);
  };

  useEffect(() => {
    if (!user && !isLoadingUser) {
      router.push("/");
    }
    fetchData();
  }, [user]);

  const handleLogout = () => {
    logout();
    router.push("/");
  };

  const handleSelectChat = async (selectedId) => {
    const chat = await chats.find((item) => item.id === selectedId);    
    setSelectedChat(chat);
  };

  return (
    <div className="container">
      <section className="app">
        <div className="left-bar">
          <div className="logged-user">
            <div className="username-wrapper">
              <span>{user?.username}</span>
            </div>
            <div className="button-wrapper">
              <button onClick={handleLogout}>
                <Image
                  src={logoutButton}
                  width={30}
                  height={30}
                  alt="logout button"
                />
              </button>
            </div>
          </div>
          <input type="text" placeholder="Search chat by username" />
          {isLoading ? (
            <Spinner />
          ) : (
            <ChatHistory chats={chats} onClickAction={handleSelectChat} />
          )}
        </div>
        <OpenChat selectedChat={selectedChat} />
      </section>
    </div>
  );
}
