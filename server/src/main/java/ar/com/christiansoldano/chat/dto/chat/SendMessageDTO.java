package ar.com.christiansoldano.chat.dto.chat;

import ar.com.christiansoldano.chat.model.chat.MessageType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SendMessageDTO(

        @JsonProperty("chat_id")
        UUID chatId,

        @NotNull
        MessageType type,

        @NotEmpty
        String content

) {
}
