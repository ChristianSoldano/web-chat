package ar.com.christiansoldano.chat.service.chat;

import ar.com.christiansoldano.chat.dto.chat.*;
import ar.com.christiansoldano.chat.exception.chat.ChatAlreadyExistsException;
import ar.com.christiansoldano.chat.exception.chat.ChatNotBelongingException;
import ar.com.christiansoldano.chat.exception.chat.ChatNotFoundException;
import ar.com.christiansoldano.chat.mapper.ChatMapper;
import ar.com.christiansoldano.chat.model.chat.Chat;
import ar.com.christiansoldano.chat.model.user.User;
import ar.com.christiansoldano.chat.repository.chat.ChatRepository;
import ar.com.christiansoldano.chat.service.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserService userService;
    private final MessageService messageService;
    private final ChatMapper chatMapper;

    @Transactional
    public ChatCreatedDTO createChat(User user, NewChatDTO newChatDTO) {
        User to = userService.findUserByUsername(newChatDTO.to())
                .orElseThrow(() -> new UsernameNotFoundException(format("Username '%s' was not found", newChatDTO.to())));
        boolean chatExists = chatRepository.existsByUser1AndUser2(user, to);
        if (chatExists) {
            throw new ChatAlreadyExistsException();
        }

        Chat chat = chatRepository.save(new Chat(user, to));
        MessageSentDTO message = messageService.saveMessage(newChatDTO.message(), user, chat);

        return chatMapper.toChatCreatedDTO(chat, message);
    }

    @Transactional
    public MessageSentDTO sendMessage(NewMessageDTO newMessageDTO, UUID chatId, User user) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatNotFoundException(format("Chat id '%s' was not found", chatId)));
        boolean userBelongsToChat = chat.getUser1().getId().equals(user.getId()) || chat.getUser2().getId().equals(user.getId());
        if (!userBelongsToChat) {
            throw new ChatNotBelongingException();
        }

        return messageService.saveMessage(newMessageDTO, user, chat);
    }

    public List<MessageSentDTO> getPreviousTenMessages(UUID chatId, UUID lastMessageId, User user) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatNotFoundException(format("Chat id '%s' was not found", chatId)));
        boolean userBelongsToChat = chatRepository.userBelongsToChat(chat, user);
        if (!userBelongsToChat) {
            throw new ChatNotBelongingException();
        }

        return messageService.getPreviousTenMessages(chat, lastMessageId);
    }

    public List<ChatDTO> getChats(User user) {
        List<Chat> chats = chatRepository.findChatsByUser(user);

        return chatMapper.toChatDTO(chats, user);
    }
}
