import { useUser } from "@/app/context/UserProvider";

export default ({ chat, onClickAction }) => {
  const { user } = useUser();

  return (
    <li onClick={() => onClickAction(chat.id)}>
      <span className="chat-history-title">{chat?.chat_with}</span>
      <span className="chat-history-message">{`${
        chat.last_message.sender === user.username
          ? "You"
          : chat.last_message.sender
      }: ${chat.last_message.content}`}</span>
    </li>
  );
};
