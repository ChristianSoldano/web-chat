package ar.com.christiansoldano.chat.service.chat;

import ar.com.christiansoldano.chat.dto.chat.*;
import ar.com.christiansoldano.chat.exception.chat.ChatAlreadyExistsException;
import ar.com.christiansoldano.chat.exception.chat.ChatNotBelongingException;
import ar.com.christiansoldano.chat.exception.chat.ChatNotFoundException;
import ar.com.christiansoldano.chat.mapper.ChatMapper;
import ar.com.christiansoldano.chat.model.chat.Chat;
import ar.com.christiansoldano.chat.model.chat.Message;
import ar.com.christiansoldano.chat.model.user.User;
import ar.com.christiansoldano.chat.repository.chat.ChatRepository;
import ar.com.christiansoldano.chat.service.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    public ChatCreatedDTO createChat(User user, CreateChatDTO createChatDTO) {
        User to = userService.findUserByUsername(createChatDTO.to())
                .orElseThrow(() -> new UsernameNotFoundException(format("Username '%s' was not found", createChatDTO.to())));
        boolean chatExists = chatRepository.existsByUser1AndUser2(user, to);
        if (chatExists) {
            throw new ChatAlreadyExistsException();
        }

        Chat chat = chatRepository.save(new Chat(user, to));
        Message message = messageService.saveMessage(createChatDTO.message(), user, chat);

        return chatMapper.toChatCreatedDTO(chat, message);
    }

    public MessageSentDTO sendMessage(SendMessageDTO sendMessageDTO, UUID chatId, User user) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatNotFoundException(format("Chat id '%s' was not found", chatId)));
        boolean userBelongsToChat = chat.getUser1().getId().equals(user.getId()) || chat.getUser2().getId().equals(user.getId());
        if (!userBelongsToChat) {
            throw new ChatNotBelongingException();
        }

        return messageService.saveMessage(sendMessageDTO, user, chat);
    }

    public Page<MessageSentDTO> getMessages(UUID chatId, User user, Pageable paging) {
        boolean userBelongsToChat = chatRepository.userBelongsToChat(chatId, user);
        if (!userBelongsToChat) {
            throw new ChatNotBelongingException();
        }

        return messageService.getMessages(chatId, paging);
    }

    public Page<ChatDTO> getChats(User user, Pageable paging) {
        Page<Chat> chatsByUser = chatRepository.findChatsByUser(user, paging);
        List<ChatDTO> chats = chatMapper.toChatDTO(chatsByUser.getContent(), user);

        return new PageImpl<>(chats, paging, chatsByUser.getTotalElements());
    }
}
