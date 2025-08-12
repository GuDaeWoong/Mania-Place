package com.example.place.domain.chatmessage.controller;

import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;
import com.example.place.common.security.jwt.CustomPrincipal;
import com.example.place.domain.chatmessage.dto.request.ChatMessageRequest;
import com.example.place.domain.chatmessage.service.ChatMessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.security.Principal;


@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chatroom/{roomId}")
    public void sendMessage(
            @DestinationVariable Long roomId,
            @Valid @Payload ChatMessageRequest request,
//            Message<?> message,
            CustomPrincipal principal
    ) {
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//        CustomPrincipal principal = (CustomPrincipal) accessor.getSessionAttributes().get("user");
//        CustomPrincipal principal = (CustomPrincipal) principal;
        if (principal == null) {
            throw new CustomException(ExceptionCode.NOT_FOUND_USER);
        }

        chatMessageService.saveMessage(roomId, request, principal);
        messagingTemplate.convertAndSend("/sub/chatroom/" + roomId, request);

    }

}
