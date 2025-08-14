package com.example.place.domain.chatroom.service;

import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;
import com.example.place.domain.chatroom.dto.response.ChatRoomResponse;
import com.example.place.domain.chatroom.entity.ChatRoom;
import com.example.place.domain.chatroom.repository.ChatRoomRepository;
import com.example.place.domain.item.entity.Item;
import com.example.place.domain.item.service.ItemService;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserService userService;
    private final ItemService itemService;

    @Transactional
    public ChatRoomResponse createChatRoom(Long buyerId, Long itemId) {

        Item item = itemService.findByIdOrElseThrow(itemId);
        User buyer = userService.findByIdOrElseThrow(buyerId);
        User seller = itemService.getSeller(itemId);

        if (buyerId.equals(seller.getId())) {
            throw new CustomException(ExceptionCode.FORBIDDEN_CHAT_WITH_SELLF);
        }


        ChatRoom chatRoom = chatRoomRepository.findExistingChatRoom(item.getId(), seller.getId(), buyer.getId())
                .orElseGet(() -> chatRoomRepository.save(ChatRoom.of(item, seller, buyer)));
        String roomId = chatRoom.getId().toString();

        return ChatRoomResponse.of(
                item.getItemName(),
                buyer.getNickname(),
                seller.getNickname(),
                "http://localhost:8080/ws/chat",
                "/sub/chatroom/" + roomId,
                "/pub/chatroom/" + roomId
        );
    }

    @Transactional
    public ChatRoom findByIdOrElseThrow (Long id) {
        return chatRoomRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_CHATROOM));
    }



}
