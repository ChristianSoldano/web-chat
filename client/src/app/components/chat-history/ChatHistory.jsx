import ChatHistoryItem from "./ChatHistoryItem";

export default ({ chats }) => {
  return (
    <ul>
      {chats?.entities?.map((item, i) => {
        return <ChatHistoryItem chat={item} key={i} />;
      })}
    </ul>
  );
};
