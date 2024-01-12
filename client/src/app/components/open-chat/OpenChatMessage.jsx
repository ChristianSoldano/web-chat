import moment from "moment";

export default ({ message, user }) => {  
  const sentAt = moment(message.createdAt).format("HH:mm");
  const messageStyle = message.sender === user.username ? "sent" : "received";

  return (
    <li className={`message ${messageStyle}`}>
      <span className="time">{sentAt}</span>
      <span>{message.content}</span>
    </li>
  );
};
