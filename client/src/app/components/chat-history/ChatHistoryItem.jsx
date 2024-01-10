export default ({ chat }) => {
  return (
    <li>
      <span className="chat-history-title">{chat.chat_with}</span>
      <span className="chat-history-message">{`${chat.last_message.sender}: ${chat.last_message.content}`}</span>
    </li>
  );
};
