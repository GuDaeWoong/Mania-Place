package com.example.place.domain.postcomment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PostCommentRequest {
	@NotBlank(message = "댓글을 입력해주세요.")
	private String content;
}
