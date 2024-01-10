import ChatHistoryItem from "./ChatHistoryItem";

export default ({ chats }) => {
  return (
    <ul>
      {chats.map((item, i) => {
        return <ChatHistoryItem chat={item} key={i} />;
      })}
    </ul>
  );
};
