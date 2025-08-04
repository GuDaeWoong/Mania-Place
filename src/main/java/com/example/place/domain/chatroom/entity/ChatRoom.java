package com.example.place.domain.chatroom.entity;

import com.example.place.domain.chatmessage.entity.ChatMessage;
import com.example.place.domain.item.entity.Item;
import com.example.place.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table
@EqualsAndHashCode(of = "id")
@EntityListeners(AuditingEntityListener.class)
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private User seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private User buyer;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ChatMessage> messages = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;

    private ChatRoom(Item item, User seller, User buyer) {
        this.item = item;
        this.seller = seller;
        this.buyer = buyer;
    }

    public static ChatRoom of(Item item, User seller, User buyer) {
        return new ChatRoom(item, seller, buyer);
    }

    public void addMessage(ChatMessage message) {
        messages.add(message);
        message.setChatRoom(this);
    }

    public void removeMessage(ChatMessage message) {
        messages.remove(message);
        message.setChatRoom(null);
    }
    public void setItem(Item item) {
        this.item = item;
    }
}
