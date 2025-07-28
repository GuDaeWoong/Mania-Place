package com.example.place.domain.post.dto.response;

import lombok.Getter;

@Getter
public class PostSearchAllResponseDto {

	private Long postId;
	private String content;
	private String mainImageUrl;
	private Long likeCount;
	private Long disLikeCount;
	private boolean isLike;
	private boolean isDislike;

	private PostSearchAllResponseDto(Long postId, String content, String mainImageUrl,
		Long likeCount, Long disLikeCount,
		boolean isLike, boolean isDislike) {
		this.postId = postId;
		this.content = content;
		this.mainImageUrl = mainImageUrl;
		this.likeCount = likeCount;
		this.disLikeCount = disLikeCount;
		this.isLike = isLike;
		this.isDislike = isDislike;
	}

	public static PostSearchAllResponseDto from(
		Long postId,
		String content,
		String mainImageUrl,
		Long likeCount,
		Long disLikeCount,
		boolean isLike,
		boolean isDislike) {
		return new PostSearchAllResponseDto(
			postId,
			content,
			mainImageUrl,
			likeCount,
			disLikeCount,
			isLike,
			isDislike
		);
	}
}
