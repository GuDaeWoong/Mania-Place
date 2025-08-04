package com.example.place.domain.chatmessage.service;

import com.example.place.domain.chatmessage.dto.request.ChatMessageRequest;
import com.example.place.domain.chatmessage.entity.ChatMessage;
import com.example.place.domain.chatmessage.repository.ChatMessageRepository;
import com.example.place.domain.chatroom.entity.ChatRoom;
import com.example.place.domain.chatroom.service.ChatRoomService;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;
    private final UserService userService;

    @Transactional
    public ChatMessage saveMessage(ChatMessageRequest request) {
        ChatRoom room = chatRoomService.findByIdOrElseThrow(request.getRoomId());
        User sender = userService.findByIdOrElseThrow(request.getSenderId());
        ChatMessage message = ChatMessage.of(room, sender, request.getContent(), null);
        return chatMessageRepository.save(message);
    }
}
