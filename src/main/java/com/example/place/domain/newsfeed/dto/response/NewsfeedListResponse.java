package com.example.place.domain.newsfeed.dto.response;

import com.example.place.domain.newsfeed.entity.Newsfeed;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NewsfeedListResponse {
	private Long id;
	private String title;
	private String content;
	private String mainImageUrl;

	private NewsfeedListResponse(Long id, String title, String content, String mainImageUrl) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.mainImageUrl = mainImageUrl;
	}

	public static NewsfeedListResponse of(Newsfeed newsfeed, String mainImageUrl) {
		return new NewsfeedListResponse(
			newsfeed.getId(),
			newsfeed.getTitle(),
			newsfeed.getContent(),
			mainImageUrl
		);
	}
}