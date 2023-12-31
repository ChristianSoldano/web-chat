package ar.com.christiansoldano.chat.dto.chat;

import ar.com.christiansoldano.chat.model.chat.MessageType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateChatMessageDTO(

        @NotNull
        MessageType type,

        @NotEmpty
        String content
) {
}
