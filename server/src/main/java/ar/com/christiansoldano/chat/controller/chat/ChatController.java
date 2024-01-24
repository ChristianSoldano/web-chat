package ar.com.christiansoldano.chat.controller.chat;

import ar.com.christiansoldano.chat.dto.chat.*;
import ar.com.christiansoldano.chat.model.user.User;
import ar.com.christiansoldano.chat.service.chat.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping
    public ResponseEntity<ChatCreatedDTO> createChat(@RequestBody @Valid NewChatDTO newChatDTO,
                                                     @AuthenticationPrincipal User user) {
        ChatCreatedDTO chatCreatedDTO = chatService.createChat(user, newChatDTO);

        return new ResponseEntity<>(chatCreatedDTO, CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ChatDTO>> getChats(
            @AuthenticationPrincipal User user
    ) {
        List<ChatDTO> chats = chatService.getChats(user);

        return ResponseEntity.ok(chats);
    }

    @PostMapping("/{chatId}/messages")
    public ResponseEntity<MessageSentDTO> sendMessage(
            @RequestBody @Valid NewMessageDTO newMessageDTO,
            @PathVariable UUID chatId,
            @AuthenticationPrincipal User user) {
        MessageSentDTO messageSentDTO = chatService.sendMessage(newMessageDTO, chatId, user);
        simpMessagingTemplate.convertAndSend(format("/chat/%s", chatId), messageSentDTO);

        return new ResponseEntity<>(messageSentDTO, CREATED);
    }

    @GetMapping("/{chatId}/messages")
    public ResponseEntity<List<MessageSentDTO>> previousTenMessages(
            @RequestParam(value = "lastMessageId", required = false) UUID lastMessageId,
            @PathVariable("chatId") UUID chatId,
            @AuthenticationPrincipal User user
    ) {
        List<MessageSentDTO> previousTenMessages = chatService.getPreviousTenMessages(chatId, lastMessageId, user);

        return ResponseEntity.ok(previousTenMessages);
    }
}
