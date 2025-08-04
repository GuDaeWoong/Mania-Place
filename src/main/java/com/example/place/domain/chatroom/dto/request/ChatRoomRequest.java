package com.example.place.domain.chatroom.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomRequest {
    @NotNull
    private Long itemId;
}
