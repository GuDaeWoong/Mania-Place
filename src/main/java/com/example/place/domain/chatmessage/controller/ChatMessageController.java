package com.example.place.domain.chatmessage.controller;

import com.example.place.domain.chatmessage.dto.request.ChatMessageRequest;
import com.example.place.domain.chatmessage.service.ChatMessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chatroom/{roomId}")
    public void sendMessage(
            @DestinationVariable Long roomId,
            @Valid @Payload ChatMessageRequest request
            ) {
        chatMessageService.saveMessage(request);
        messagingTemplate.convertAndSend("/sub/chatroom/" + roomId, request);
    }
}
