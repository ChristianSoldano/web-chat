package ar.com.christiansoldano.chat.controller.chat;

import ar.com.christiansoldano.chat.dto.chat.NewMessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LiveChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/message/{channel}")
    public void sendMessage(NewMessageDTO message, @DestinationVariable String channel) {
        simpMessagingTemplate.convertAndSend("/chat/" + channel, message);
    }
}
