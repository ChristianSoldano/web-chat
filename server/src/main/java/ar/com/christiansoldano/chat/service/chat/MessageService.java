package ar.com.christiansoldano.chat.service.chat;

import ar.com.christiansoldano.chat.dto.chat.CreateChatMessageDTO;
import ar.com.christiansoldano.chat.dto.chat.MessageSentDTO;
import ar.com.christiansoldano.chat.dto.chat.SendMessageDTO;
import ar.com.christiansoldano.chat.mapper.MessageMapper;
import ar.com.christiansoldano.chat.model.chat.Chat;
import ar.com.christiansoldano.chat.model.chat.Message;
import ar.com.christiansoldano.chat.model.user.User;
import ar.com.christiansoldano.chat.repository.chat.MessageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    @Transactional
    public Message saveMessage(CreateChatMessageDTO createChatMessageDTO, User user, Chat chat) {
        Message message = messageMapper.mapToMessage(createChatMessageDTO, chat, user);

        return messageRepository.save(message);
    }

    @Transactional
    public MessageSentDTO saveMessage(SendMessageDTO sendMessageDTO, User user, Chat chat) {
        Message message = messageMapper.mapToMessage(sendMessageDTO, chat, user);
        message = messageRepository.save(message);

        return messageMapper.toMessageSentDTO(message);
    }

    public List<MessageSentDTO> getPreviousTenMessages(UUID chatId, UUID messageId) {
        List<Message> messages;
        if (messageId == null) {
            messages = messageRepository.findFirst20ByChat_IdOrderByCreatedAtDesc(chatId);
        } else {
            messages = messageRepository.findPreviousByChatIdAndMessageId(chatId, messageId);
        }

        return messageMapper.toMessageSentDTO(messages);
    }

    public MessageSentDTO getLastMessageByChat(UUID chatId) {
        Message message = messageRepository.findFirstByChat_IdOrderByCreatedAtDesc(chatId)
                .orElse(null);

        return messageMapper.toMessageSentDTO(message);
    }
}
