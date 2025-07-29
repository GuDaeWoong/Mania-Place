package com.example.place.domain.vote.dto.response;

import lombok.Getter;

@Getter
public class VoteResponseDto {

	private Long likeCount;
	private Long disLikeCount;
	private boolean isLike;
	private boolean isDislike;

	private VoteResponseDto(Long likeCount, Long dislikeCount, boolean isLike, boolean isDislike) {
		this.likeCount = likeCount;
		this.disLikeCount = dislikeCount;
		this.isLike = isLike;
		this.isDislike = isDislike;

	}

	public static VoteResponseDto of(Long likeCount, Long disLikeCount, boolean isLike, boolean isDislike) {
		return new VoteResponseDto(likeCount, disLikeCount, isLike, isDislike);
	}

	// 투표 정보가 없는 경우
	public static VoteResponseDto empty() {
		return new VoteResponseDto(0L, 0L, false, false);
	}
}
