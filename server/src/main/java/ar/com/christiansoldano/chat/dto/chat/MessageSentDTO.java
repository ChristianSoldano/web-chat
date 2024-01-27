package ar.com.christiansoldano.chat.dto.chat;

import ar.com.christiansoldano.chat.model.chat.MessageType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.util.UUID;

public record MessageSentDTO(

        UUID id,

        @JsonProperty("chat_id")
        UUID chatId,

        MessageType type,

        String content,

        @JsonProperty("created_at")
        Timestamp createdAt,

        String sender
) {
}
