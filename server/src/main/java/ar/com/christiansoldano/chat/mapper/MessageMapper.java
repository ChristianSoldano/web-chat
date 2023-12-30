package ar.com.christiansoldano.chat.mapper;

import ar.com.christiansoldano.chat.dto.chat.CreateChatMessageDTO;
import ar.com.christiansoldano.chat.dto.chat.SendMessageDTO;
import ar.com.christiansoldano.chat.dto.chat.MessageSentDTO;
import ar.com.christiansoldano.chat.model.chat.Chat;
import ar.com.christiansoldano.chat.model.chat.Message;
import ar.com.christiansoldano.chat.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface MessageMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Message mapToMessage(CreateChatMessageDTO createChatMessageDTO, Chat chat, User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Message mapToMessage(SendMessageDTO sendMessageDTO, Chat chat, User user);

    @Mapping(target = "sender", source = "user.username")
    MessageSentDTO toMessageSentDTO(Message message);

    List<MessageSentDTO> toMessageSentDTO(List<Message> message);
}
