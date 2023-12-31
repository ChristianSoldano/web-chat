package ar.com.christiansoldano.chat.mapper;

import ar.com.christiansoldano.chat.dto.chat.ChatCreatedDTO;
import ar.com.christiansoldano.chat.dto.chat.ChatDTO;
import ar.com.christiansoldano.chat.model.chat.Chat;
import ar.com.christiansoldano.chat.model.chat.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;

@Mapper(uses = {MessageMapper.class}, injectionStrategy = CONSTRUCTOR)
public interface ChatMapper {

    @Mapping(target = "chatId", source = "chat.id")
    @Mapping(target = "username1", source = "chat.user1.username")
    @Mapping(target = "username2", source = "chat.user2.username")
    ChatCreatedDTO toChatCreatedDTO(Chat chat, Message message);

    @Mapping(target = "username1", source = "user1.username")
    @Mapping(target = "username2", source = "user2.username")
    ChatDTO toChatDTO(Chat chat);

    List<ChatDTO> toChatDTO(List<Chat> chat);
}
