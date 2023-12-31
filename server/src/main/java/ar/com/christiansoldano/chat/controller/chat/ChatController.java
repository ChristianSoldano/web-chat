package ar.com.christiansoldano.chat.controller.chat;

import ar.com.christiansoldano.chat.dto.chat.*;
import ar.com.christiansoldano.chat.dto.paging.PaginatedEntityDTO;
import ar.com.christiansoldano.chat.model.user.User;
import ar.com.christiansoldano.chat.service.chat.ChatService;
import ar.com.christiansoldano.chat.util.Paging;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ChatCreatedDTO> createChat(@RequestBody @Valid CreateChatDTO createChatDTO,
                                                     @AuthenticationPrincipal User user) {
        ChatCreatedDTO chatCreatedDTO = chatService.createChat(user, createChatDTO);

        return new ResponseEntity<>(chatCreatedDTO, CREATED);
    }

    @GetMapping
    public ResponseEntity<PaginatedEntityDTO<ChatDTO>> getChats(
            @Range(min = 1, max = 20) @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @Range(min = 1) @RequestParam(value = "page", defaultValue = "1") int page,
            @AuthenticationPrincipal User user
    ) {
        Pageable paging = Paging.of(page, pageSize);
        Page<ChatDTO> chatsPaged = chatService.getChats(user, paging);
        PaginatedEntityDTO<ChatDTO> paginatedResponse = PaginatedEntityDTO.fromPage(chatsPaged);

        return ResponseEntity.ok(paginatedResponse);
    }

    @PostMapping("/{chatId}/messages")
    public ResponseEntity<MessageSentDTO> sendMessage(@RequestBody @Valid SendMessageDTO sendMessageDTO,
                                                      @PathVariable UUID chatId,
                                                      @AuthenticationPrincipal User user) {
        MessageSentDTO messageSentDTO = chatService.sendMessage(sendMessageDTO, chatId, user);
        simpMessagingTemplate.convertAndSend(format("/chat/%s", chatId), messageSentDTO);

        return new ResponseEntity<>(messageSentDTO, CREATED);
    }

    @GetMapping("/{chatId}/messages")
    public ResponseEntity<PaginatedEntityDTO<MessageSentDTO>> getMessages(
            @Range(min = 1, max = 20) @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @Range(min = 1) @RequestParam(value = "page", defaultValue = "1") int page,
            @PathVariable("chatId") UUID chatId,
            @AuthenticationPrincipal User user
    ) {
        Pageable paging = Paging.of(page, pageSize);
        Page<MessageSentDTO> messagesPaged = chatService.getMessages(chatId, user, paging);
        PaginatedEntityDTO<MessageSentDTO> paginatedResponse = PaginatedEntityDTO.fromPage(messagesPaged);

        return ResponseEntity.ok(paginatedResponse);
    }
}
