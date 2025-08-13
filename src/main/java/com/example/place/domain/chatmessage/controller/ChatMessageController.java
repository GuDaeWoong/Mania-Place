package com.example.place.domain.chatmessage.controller;

import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;
import com.example.place.common.security.jwt.CustomPrincipal;
import com.example.place.domain.chatmessage.dto.request.ChatMessageRequest;
import com.example.place.domain.chatmessage.dto.request.MessageQueue;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;


@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final SimpMessagingTemplate messagingTemplate;
    private final RabbitTemplate rabbitTemplate;
    private static final String CHAT_QUEUE_NAME = "chat.queue";

    @MessageMapping("/chatroom/{roomId}")
    public void sendMessage(
            @DestinationVariable Long roomId,
            @Valid @Payload ChatMessageRequest request,
            CustomPrincipal principal
    ) {
        if (principal == null) {
            throw new CustomException(ExceptionCode.NOT_FOUND_USER);
        }
        MessageQueue messageQueue = MessageQueue.of(request.getContent(), principal.getId(), roomId, LocalDateTime.now());
        rabbitTemplate.convertAndSend(CHAT_QUEUE_NAME, messageQueue);
        messagingTemplate.convertAndSend("/sub/chatroom/" + roomId, messageQueue);
    }
}
