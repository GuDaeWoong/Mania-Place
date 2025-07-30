package com.example.place.domain.newsfeed.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.example.place.domain.Image.entity.Image;
import com.example.place.domain.newsfeed.entity.Newsfeed;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NewsfeedResponse {

	private Long id;
	private String title;
	private String content;
	private List<String> imageUrls;
	private int mainIndex;

	public static com.example.place.domain.newsfeed.dto.response.NewsfeedResponse from(Newsfeed newsfeed) {
		List<Image> images = newsfeed.getImages();
		List<String> imageUrls = new ArrayList<>();
		int mainIndex = 0;
		for (int i = 0; i < images.size(); i++) {
			imageUrls.add(images.get(i).getImageUrl());
			if (images.get(i).isMain()) {
				mainIndex = i;
			}
		}

		com.example.place.domain.newsfeed.dto.response.NewsfeedResponse response = new com.example.place.domain.newsfeed.dto.response.NewsfeedResponse();
		response.id = newsfeed.getId();
		response.title = newsfeed.getTitle();
		response.content = newsfeed.getContent();
		response.imageUrls = imageUrls;
		response.mainIndex = mainIndex;
		return response;
	}
}
