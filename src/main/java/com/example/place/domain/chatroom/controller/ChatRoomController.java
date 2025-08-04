package com.example.place.domain.chatroom.controller;

import com.example.place.common.dto.ApiResponseDto;
import com.example.place.common.security.jwt.CustomPrincipal;
import com.example.place.domain.chatroom.dto.request.ChatRoomRequest;
import com.example.place.domain.chatroom.dto.response.ChatRoomResponse;
import com.example.place.domain.chatroom.service.ChatRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chatroom")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping
    public ResponseEntity<ApiResponseDto<ChatRoomResponse>> createChatRoom(
            @AuthenticationPrincipal CustomPrincipal principal,
            @Valid @RequestBody ChatRoomRequest request
    ) {
        ChatRoomResponse response = chatRoomService.createChatRoom(principal.getId(), request.getItemId());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.of("채팅방에 입장하셨습니다.", response));
    }


}
