package com.example.place.domain.chatmessage.service;

import com.example.place.domain.chatmessage.entity.ChatMessage;
import com.example.place.domain.chatmessage.dto.request.MessageQueue;
import com.example.place.domain.chatmessage.repository.ChatMessageRepository;
import com.example.place.domain.chatroom.entity.ChatRoom;
import com.example.place.domain.chatroom.service.ChatRoomService;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;
    private final UserService userService;
    private final List<MessageQueue> messageBuffer = new CopyOnWriteArrayList<>();

    @RabbitListener(queues = "chat.queue")
    public void consumeAndBufferMessage(MessageQueue request) {
        messageBuffer.add(request);
        log.info("버퍼에 메세지 추가: {}, 담긴메세지 개수: {}", request.getContent(), messageBuffer.size());
    }

    @Scheduled(fixedDelay = 20000)
    public void saveMessageFromBuffer() {
        if(messageBuffer.isEmpty()) {
            return;
        }
        List<MessageQueue> messageSave = new ArrayList<>(messageBuffer);
        messageBuffer.clear();

        List<ChatMessage> chatMessages = messageSave.stream()
                .map(this::createChatMessageFromQueue)
                .toList();
        chatMessageRepository.saveAll(chatMessages);
        log.info("버퍼에 있는 메세지 {} 개 20초마다 일괄저장", chatMessages.size());
    }

    private ChatMessage createChatMessageFromQueue(MessageQueue messageQueue) {
        User sender = userService.findByIdOrElseThrow(messageQueue.getSenderId());
        ChatRoom chatRoom = chatRoomService.findByIdOrElseThrow(messageQueue.getRoomId());
        return ChatMessage.of(chatRoom, sender, messageQueue.getContent(), messageQueue.getSentAt());
    }

}

