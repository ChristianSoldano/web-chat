package ar.com.christiansoldano.chat.mapper;

import ar.com.christiansoldano.chat.dto.chat.MessageSentDTO;
import ar.com.christiansoldano.chat.dto.chat.NewMessageDTO;
import ar.com.christiansoldano.chat.model.chat.Chat;
import ar.com.christiansoldano.chat.model.chat.Message;
import ar.com.christiansoldano.chat.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface MessageMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Message mapToMessage(NewMessageDTO newMessageDTO, Chat chat, User user);

    @Mapping(target = "id", source = "message.id")
    @Mapping(target = "chatId", source = "chat.id")
    @Mapping(target = "createdAt", source = "message.createdAt")
    @Mapping(target = "sender", source = "message.user.username")
    MessageSentDTO toMessageSentDTO(Message message, Chat chat);

    default List<MessageSentDTO> toMessageSentDTO(List<Message> messages, Chat chat) {
        return messages.stream()
                .map(message -> toMessageSentDTO(message, chat))
                .toList();
    }
}
