package com.example.place.domain.chatroom.repository;

import com.example.place.domain.chatroom.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("SELECT c FROM ChatRoom c " +
            "WHERE c.item.id = :itemId AND c.seller.id = :sellerId AND c.buyer.id = :buyerId")
    Optional<ChatRoom> findExistingChatRoom(@Param("itemId") Long itemId,
                                            @Param("sellerId") Long sellerId,
                                            @Param("buyerId") Long buyerId);
}
