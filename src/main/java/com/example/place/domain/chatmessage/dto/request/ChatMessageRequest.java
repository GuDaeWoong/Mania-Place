package com.example.place.domain.chatmessage.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatMessageRequest {
    @NotNull(message = "채팅방은 필수입니다.")
    private Long roomId;
    @NotNull(message = "발신인은 필수입니다.")
    private Long senderId;
    @NotBlank(message = "내용은 필수입니다.")
    private String content;
}
