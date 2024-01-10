package ar.com.christiansoldano.chat.dto.chat;

import ar.com.christiansoldano.chat.model.chat.MessageType;

public record LastMessageDTO(

        MessageType type,
        String content,
        String sender
) {
}
