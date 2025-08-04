package com.example.place.domain.chatroom.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomResponse {
    private String itemName;
    private String buyerNickName;
    private String sellerNickName;

    private String websocketUrl;
    private String subscribeDestination;
    private String publishDestination;

    public static ChatRoomResponse of(
            String itemName,
            String buyerNickName,
            String sellerNickName,
            String websocketUrl,
            String subscribeDestination,
            String publishDestination
    ) {
        ChatRoomResponse response = new ChatRoomResponse();
        response.itemName = itemName;
        response.buyerNickName = buyerNickName;
        response.sellerNickName = sellerNickName;
        response.websocketUrl = websocketUrl;
        response.subscribeDestination = subscribeDestination;
        response.publishDestination = publishDestination;
        return response;
    }
}
