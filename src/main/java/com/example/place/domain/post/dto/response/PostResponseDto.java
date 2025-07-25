package com.example.place.domain.post.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.example.place.domain.Image.entity.Image;
import com.example.place.domain.post.entity.Post;

import lombok.Getter;

@Getter
public class PostResponseDto {
	private final String content;
	private final String nickname;
	private final Long itemId;
	private List<String> imageUrls;
	private int mainIndex;

	private PostResponseDto(Long id, String content, String nickname, Long itemId, List<String> imageUrls,
		int mainIndex) {
		this.content = content;
		this.nickname = nickname;
		this.itemId = itemId;
		this.imageUrls = imageUrls;
		this.mainIndex = mainIndex;
	}

	public static PostResponseDto from(Post post) {
		List<Image> images = post.getItem().getImages();
		List<String> imageUrls = new ArrayList<>();
		int mainIndex = 0;
		for (int i = 0; i < images.size(); i++) {
			imageUrls.add(images.get(i).getImageUrl());
			if (images.get(i).isMain()) {
				mainIndex = i;
			}
		}

		return new PostResponseDto(post.getId(),
			post.getContent(),
			post.getUser().getNickname(),
			post.getItem().getId(),
			imageUrls,
			mainIndex);
	}

	public static PostResponseDto fromWithImages(Post post, List<Image> images) {
		List<String> imageUrls = new ArrayList<>();
		int mainIndex = 0;
		for (int i = 0; i < images.size(); i++) {
			imageUrls.add(images.get(i).getImageUrl());
			if (images.get(i).isMain()) {
				mainIndex = i;
			}
		}

		return new PostResponseDto(post.getId(),
			post.getContent(),
			post.getUser().getNickname(),
			post.getItem().getId(),
			imageUrls,
			mainIndex);
	}

}