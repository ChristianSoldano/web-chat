package ar.com.christiansoldano.chat.service.chat;

import ar.com.christiansoldano.chat.dto.chat.MessageSentDTO;
import ar.com.christiansoldano.chat.dto.chat.NewMessageDTO;
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
    public MessageSentDTO saveMessage(NewMessageDTO newMessageDTO, User user, Chat chat) {
        Message message = messageMapper.mapToMessage(newMessageDTO, chat, user);
        message = messageRepository.save(message);

        return messageMapper.toMessageSentDTO(message, chat);
    }

    public List<MessageSentDTO> getPreviousTenMessages(Chat chat, UUID messageId) {
        List<Message> messages;
        if (messageId == null) {
            messages = messageRepository.findFirst20ByChat_IdOrderByCreatedAtDesc(chat.getId());
        } else {
            messages = messageRepository.findPreviousByChatIdAndMessageId(chat.getId(), messageId);
        }

        return messageMapper.toMessageSentDTO(messages, chat);
    }

    public MessageSentDTO getLastMessageByChat(Chat chat) {
        Message message = messageRepository.findFirstByChat_IdOrderByCreatedAtDesc(chat.getId())
                .orElse(null);

        return messageMapper.toMessageSentDTO(message, chat);
    }
}
