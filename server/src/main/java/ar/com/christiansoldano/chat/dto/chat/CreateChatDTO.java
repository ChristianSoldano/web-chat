package ar.com.christiansoldano.chat.dto.chat;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateChatDTO(

        @NotEmpty
        String to,

        @NotNull
        @Valid
        CreateChatMessageDTO message
) {
}
