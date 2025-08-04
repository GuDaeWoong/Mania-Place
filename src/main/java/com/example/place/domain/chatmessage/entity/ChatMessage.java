package com.example.place.domain.chatmessage.entity;

import com.example.place.domain.chatroom.entity.ChatRoom;
import com.example.place.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table
@EntityListeners(AuditingEntityListener.class)
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    private String message;
    @CreatedDate
    private LocalDateTime sentAt;

    private ChatMessage(ChatRoom chatRoom, User sender, String message, LocalDateTime sentAt) {
        this.chatRoom = chatRoom;
        this.sender = sender;
        this.message = message;
        this.sentAt = sentAt;
    }

    public static ChatMessage of(ChatRoom chatRoom, User sender, String message, LocalDateTime sentAt) {
        return new ChatMessage(chatRoom, sender, message, sentAt);
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    public void setSender(User user) {
        this.sender = user;
    }
}