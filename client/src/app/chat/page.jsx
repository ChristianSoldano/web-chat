"use client";
import ChatHistory from "@/app/components/chat-history/ChatHistory";
import OpenChat from "@/app/components/open-chat/OpenChat";
import { getCookie } from "cookies-next";
import { useEffect, useState } from "react";

export default function Chat() {
  const [chats, setChats] = useState([]);
  const [selectedChatId, setSelectedChatId] = useState("");

  const fetchChat = async () => {
    const session = JSON.parse(getCookie("session"));
    const response = await fetch("http://localhost:8080/api/chats", {
      method: "GET",
      headers: {
        "content-type": "application/json",
        Authorization: "Bearer " + session.access_token,
      },
    });
    const data = await response.json();

    setChats(data.entities);
    setSelectedChatId(data.entities[0].id);
  };

  useEffect(() => {
    fetchChat();
  }, []);

  return (
    <div className="container">
      <section className="app">
        <div className="left-bar">
          <input type="text" placeholder="Search chat by username" />
          <ChatHistory chats={chats} />
        </div>
        {selectedChatId && <OpenChat selectedChatId={selectedChatId} />}
      </section>
    </div>
  );
}
