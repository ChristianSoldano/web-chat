package ar.com.christiansoldano.chat.mapper;

import ar.com.christiansoldano.chat.dto.chat.ChatCreatedDTO;
import ar.com.christiansoldano.chat.dto.chat.ChatDTO;
import ar.com.christiansoldano.chat.dto.chat.LastMessageDTO;
import ar.com.christiansoldano.chat.dto.chat.MessageSentDTO;
import ar.com.christiansoldano.chat.model.chat.Chat;
import ar.com.christiansoldano.chat.model.chat.Message;
import ar.com.christiansoldano.chat.model.user.User;
import ar.com.christiansoldano.chat.service.chat.MessageService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;

@Mapper(uses = {MessageMapper.class}, injectionStrategy = CONSTRUCTOR)
public abstract class ChatMapper {

    @Autowired
    MessageService messageService;

    @Mapping(target = "chatId", source = "chat.id")
    @Mapping(target = "username1", source = "chat.user1.username")
    @Mapping(target = "username2", source = "chat.user2.username")
    public abstract ChatCreatedDTO toChatCreatedDTO(Chat chat, Message message);

    @Mapping(target = "id", source = "chat.id")
    @Mapping(target = "createdAt", source = "chat.createdAt")
    @Mapping(target = "chatWith", expression = "java(chat.getUser1().equals(user) ? chat.getUser2().getUsername() : chat.getUser1().getUsername())")
    public abstract ChatDTO toChatDTO(Chat chat, User user);

    public List<ChatDTO> toChatDTO(List<Chat> chats, User user) {
        List<ChatDTO> result = new ArrayList<>();
        for (Chat chat : chats) {
            ChatDTO chatDTO = toChatDTO(chat, user);
            result.add(chatDTO);
        }

        return result;
    }

    @AfterMapping
    protected void afterMappingChatDTO(@MappingTarget ChatDTO dto, Chat chat) {
        MessageSentDTO lastMessage = messageService.getLastMessageByChat(chat.getId());
        if (lastMessage == null) {
            return;
        }

        LastMessageDTO lastMessageDTO = new LastMessageDTO(lastMessage.type(), lastMessage.content(), lastMessage.sender());
        dto.setLastMessage(lastMessageDTO);
    }
}
