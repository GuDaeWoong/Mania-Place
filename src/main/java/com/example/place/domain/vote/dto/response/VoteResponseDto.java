package com.example.place.domain.vote.dto.response;

import lombok.Getter;

@Getter
public class VoteResponseDto {
	private Long likeCount;
	private Long disLikeCount;

	private VoteResponseDto(Long likeCount, Long dislikeCount) {
		this.likeCount = likeCount;
		this.disLikeCount = dislikeCount;
	}

	public static VoteResponseDto of(Long likeCount, Long disLikeCount) {
		return new VoteResponseDto(likeCount, disLikeCount);
	}
}
