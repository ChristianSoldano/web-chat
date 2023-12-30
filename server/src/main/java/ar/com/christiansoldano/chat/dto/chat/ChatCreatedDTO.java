package ar.com.christiansoldano.chat.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ChatCreatedDTO(

        @JsonProperty("chat_id")
        String chatId,

        String username1,

        String username2,

        MessageSentDTO message
) {
}
