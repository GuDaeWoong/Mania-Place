package com.example.place.domain.chatmessage.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageQueue {

    private String content;
    private Long senderId;
    private Long roomId;
    private LocalDateTime sentAt;

    private MessageQueue(String content, Long senderId, Long roomId, LocalDateTime sentAt) {
        this.content = content;
        this.senderId = senderId;
        this. roomId = roomId;
        this.sentAt = sentAt;
    }

    public static MessageQueue of(String content, Long senderId, Long roomId, LocalDateTime sentAt) {
        return new MessageQueue(content, senderId, roomId, sentAt);
    }
}
