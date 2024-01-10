import moment from "moment";

const loggedUser = "user1";

export default ({ message }) => {
  const sentAt = moment(message.createdAt).format("HH:mm");
  const messageStyle = message.sender === loggedUser ? "sent" : "received";

  return (
    <li className={`message ${messageStyle}`}>
      <span className="time">{sentAt}</span>
      <span>{message.content}</span>
    </li>
  );
};
