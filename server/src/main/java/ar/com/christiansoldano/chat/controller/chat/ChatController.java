package ar.com.christiansoldano.chat.controller.chat;

import ar.com.christiansoldano.chat.dto.chat.ChatCreatedDTO;
import ar.com.christiansoldano.chat.dto.chat.CreateChatDTO;
import ar.com.christiansoldano.chat.dto.chat.MessageSentDTO;
import ar.com.christiansoldano.chat.dto.chat.SendMessageDTO;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/create")
    public ResponseEntity<ChatCreatedDTO> createChat(@RequestBody @Valid CreateChatDTO createChatDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        ChatCreatedDTO chatCreatedDTO = chatService.createChat(user, createChatDTO);

        return new ResponseEntity<>(chatCreatedDTO, CREATED);
    }

    @PostMapping("/send")
    public ResponseEntity<MessageSentDTO> sendMessage(@RequestBody @Valid SendMessageDTO sendMessageDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        MessageSentDTO messageSentDTO = chatService.sendMessage(sendMessageDTO, user);

        return new ResponseEntity<>(messageSentDTO, CREATED);
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<PaginatedEntityDTO<MessageSentDTO>> getMessages(
            @Range(min = 1, max = 20) @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @Range(min = 1) @RequestParam(value = "page", defaultValue = "1") int page,
            @PathVariable("chatId") UUID chatId
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        Pageable paging = Paging.of(page, pageSize);
        Page<MessageSentDTO> messagesPaged = chatService.getMessages(chatId, user, paging);
        PaginatedEntityDTO<MessageSentDTO> paginatedResponse = PaginatedEntityDTO.fromPage(messagesPaged);

        return ResponseEntity.ok(paginatedResponse);
    }
}
