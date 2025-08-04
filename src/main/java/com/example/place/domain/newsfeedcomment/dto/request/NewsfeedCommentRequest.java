package com.example.place.domain.newsfeedcomment.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class NewsfeedCommentRequest {
	@NotBlank(message = "댓글을 입력해주세요.")
	private String content;
}
