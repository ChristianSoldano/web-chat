"use client";
import ChatHistory from "@/app/components/chat-history/ChatHistory";
import OpenChat from "@/app/components/open-chat/OpenChat";
import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { useUser } from "../context/UserProvider";
import { fetchChats } from "../services/api";
import Spinner from "../components/Spinner";

export default function Chat() {
  const router = useRouter();
  const { user, logout, isLoadingUser } = useUser();
  const [chats, setChats] = useState([]);
  const [selectedChatId, setSelectedChatId] = useState("");
  const [isLoading, setIsLoading] = useState(true);

  const fetchData = async () => {
    if (user && !isLoadingUser) {
      setIsLoading(true);
      const data = await fetchChats(user);
      setChats(data);
      setSelectedChatId(data.entities[0].id);
      setIsLoading(false);
    }
  };

  useEffect(() => {
    if (!user && !isLoadingUser) {
      router.push("/");
    }
    fetchData();
  }, [user]);

  return (
    <div className="container">
      <section className="app">
        <div className="left-bar">
          <input type="text" placeholder="Search chat by username" />
          {isLoading ? <Spinner /> : <ChatHistory chats={chats} />}
        </div>
        <OpenChat selectedChatId={selectedChatId} />
      </section>
    </div>
  );
}
