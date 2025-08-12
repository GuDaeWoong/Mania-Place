package com.example.place.domain.chatmessage.service;

import com.example.place.common.security.jwt.CustomPrincipal;
import com.example.place.domain.chatmessage.dto.request.ChatMessageRequest;
import com.example.place.domain.chatmessage.entity.ChatMessage;
import com.example.place.domain.chatmessage.repository.ChatMessageRepository;
import com.example.place.domain.chatroom.entity.ChatRoom;
import com.example.place.domain.chatroom.service.ChatRoomService;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;
    private final UserService userService;

    @Transactional
    public ChatMessage saveMessage(Long roomId,ChatMessageRequest request, CustomPrincipal principal) {
        User sender = userService.findByIdOrElseThrow(principal.getId());
        log.info("sender 는 들어오나 {}", sender);
        String nickName = sender.getNickname();
        String image = sender.getImageUrl();
        log.info("유저 닉네임  {}", nickName);
        ChatRoom room = chatRoomService.findByIdOrElseThrow(roomId);
//        User sender = userService.findByIdOrElseThrow(request.getSenderId());
        ChatMessage message = ChatMessage.of(room, sender, request.getContent(), null);
        return chatMessageRepository.save(message);
    }
}
