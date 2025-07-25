package com.example.place.domain.post.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.example.place.domain.Image.entity.Image;
import com.example.place.domain.post.entity.Post;

import lombok.Getter;

@Getter
public class PostWithUserResponseDto {
	private final Long id;
	private final String content;
	private final String nickname;
	private final Long itemId;
	private List<String> imageUrls;
	private int mainIndex;

	public PostWithUserResponseDto(Post post,String nickname) {
		List<Image> images = post.getItem().getImages();
		List<String> imageUrls = new ArrayList<>();
		int mainIndex = 0;
		for (int i = 0; i < images.size(); i++) {
			imageUrls.add(images.get(i).getImageUrl());
			if (images.get(i).isMain()) {
				mainIndex = i;
			}
		}

		this.id = post.getId();
		this.content = post.getContent();
		this.nickname = nickname;
		this.itemId = post.getItem().getId();
		this.imageUrls = imageUrls;
		this.mainIndex = mainIndex;
	}
}