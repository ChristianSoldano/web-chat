package ar.com.christiansoldano.chat.service.chat;

import ar.com.christiansoldano.chat.dto.chat.ChatCreatedDTO;
import ar.com.christiansoldano.chat.dto.chat.CreateChatDTO;
import ar.com.christiansoldano.chat.dto.chat.MessageSentDTO;
import ar.com.christiansoldano.chat.dto.chat.SendMessageDTO;
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
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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

    public MessageSentDTO sendMessage(SendMessageDTO sendMessageDTO, User user) {
        Chat chat = chatRepository.findById(sendMessageDTO.chatId())
                .orElseThrow(() -> new ChatNotFoundException(format("Chat id '%s' was not found", sendMessageDTO.chatId())));
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
}
