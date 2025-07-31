package com.example.place.domain.newsfeed.dto.response;

import java.time.LocalDateTime;

import com.example.place.domain.newsfeed.entity.Newsfeed;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NewsfeedListResponse {
	private Long id;
	private String title;
	private String mainImageUrl;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	private NewsfeedListResponse(Long id, String title, String mainImageUrl, LocalDateTime createdAt,
		LocalDateTime updatedAt) {
		this.id = id;
		this.title = title;
		this.mainImageUrl = mainImageUrl;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public static NewsfeedListResponse of(Newsfeed newsfeed, String mainImageUrl) {
		return new NewsfeedListResponse(
			newsfeed.getId(),
			newsfeed.getTitle(),
			mainImageUrl,
			newsfeed.getCreatedAt(),
			newsfeed.getUpdatedAt()
		);
	}
}