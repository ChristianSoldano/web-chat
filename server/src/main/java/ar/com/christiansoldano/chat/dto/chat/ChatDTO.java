package ar.com.christiansoldano.chat.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Setter
@Getter
public class ChatDTO {

    private UUID id;

    @JsonProperty("chat_with")
    private String chatWith;

    @JsonProperty("last_message")
    private MessageSentDTO lastMessage;

    @JsonProperty("created_at")
    private Timestamp createdAt;
}
