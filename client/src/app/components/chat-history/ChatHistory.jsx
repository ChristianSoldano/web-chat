import ChatHistoryItem from "./ChatHistoryItem";

export default ({ chats, onClickAction }) => {  
  return (
    <ul>
      {chats.map((item, i) => {
        return (
          <ChatHistoryItem chat={item} onClickAction={onClickAction} key={i} />
        );
      })}
    </ul>
  );
};
