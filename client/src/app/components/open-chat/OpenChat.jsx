"use client";
import { useEffect, useRef, useState } from "react";

import OpenChatItem from "./OpenChatMessage";
import Image from "next/image";
import sendMessageIcon from "@/../public/send-button.svg";
import Spinner from "../Spinner";
import { useUser } from "@/app/context/UserProvider";
import { fetchMessages } from "@/app/services/api";

export default ({ selectedChatId }) => {
  const [messages, setMessages] = useState([]);
  const { user, isLoadingUser } = useUser();
  const [isLoading, setIsLoading] = useState(true);
  const messagesRef = useRef(null);

  const fetchData = async () => {
    if (user && !isLoadingUser && selectedChatId) {
      setIsLoading(true);
      const data = await fetchMessages(user, selectedChatId);
      setMessages(data.entities);
      setIsLoading(false);
    }
  };

  const scrollDown = () => {
    if (messagesRef.current) {
      messagesRef.current.firstElementChild?.scrollIntoView();
    }
  };

  useEffect(() => {
    scrollDown();
  }, [messages]);

  useEffect(() => {
    fetchData();
  }, [selectedChatId]);

  return (
    <div className="open-chat">
      <div className="contact-header">Alen Massetta</div>
      <div className="chat">
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
