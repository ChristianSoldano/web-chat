"use client";
import { useEffect, useRef, useState } from "react";
import OpenChatItem from "./OpenChatMessage";
import Image from "next/image";
import sendMessageIcon from "@/../public/send-button.svg";
import { getCookie } from "cookies-next";
import Spinner from "../Spinner";
export default ({ selectedChatId }) => {
  const [messages, setMessages] = useState([]);
  const chatRef = useRef(null);

  const fetchMessages = async () => {
    const session = JSON.parse(getCookie("session"));
    const url = `http://localhost:8080/api/chats/${selectedChatId}/messages`;
    const response = await fetch(url, {
      method: "GET",
      headers: {
        "content-type": "application/json",
        Authorization: "Bearer " + session.access_token,
      },
    });
    const data = await response.json();

    setMessages(data.entities);
  };

  useEffect(() => {
    if (!selectedChatId) {
      return;
    }

    fetchMessages();
    // const chat = document.querySelector(".chat");
    // const { height } = chatRef.current.getBoundingClientRect();
    // chat.scrollTo({ top: height, left: 0, behavior: "instant" });
    // chat.classList.add("is-loaded");
  }, [selectedChatId]);

  return (
    <div className="open-chat">
      <div className="contact-header">Alen Massetta</div>
      <div className="chat">
        {messages ? (
          <ul ref={chatRef}>
            {messages.map((message, i) => {
              return <OpenChatItem message={message} key={i} />;
            })}
          </ul>
        ) : (
          <Spinner />
        )}
      </div>
      <div className="send">
        <input type="text" placeholder="Type a message" />
        <button>
          <Image
            src={sendMessageIcon}
            width={40}
            height={40}
            alt="send button"
            className="send-button"
          />
        </button>
      </div>
    </div>
  );
};
