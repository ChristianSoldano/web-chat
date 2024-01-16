export default ({ chat, onClickAction }) => {
  return (
    <li onClick={() => onClickAction(chat.id)}>
      <span className="chat-history-title">{chat?.chat_with}</span>
      <span className="chat-history-message">{`${chat.last_message.sender}: ${chat.last_message.content}`}</span>
    </li>
  );
};
