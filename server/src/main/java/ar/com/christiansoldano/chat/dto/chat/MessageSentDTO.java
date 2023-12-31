package ar.com.christiansoldano.chat.dto.chat;

import ar.com.christiansoldano.chat.model.chat.MessageType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public record MessageSentDTO(

        MessageType type,

        String content,

        @JsonProperty("created_at")
        Timestamp createdAt,

        String sender
) {
}
