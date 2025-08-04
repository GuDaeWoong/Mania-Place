package com.example.place.domain.post.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.example.place.domain.Image.dto.ImageDto;
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

	private PostResponseDto(String content, String nickname, Long itemId, List<String> imageUrls,
		int mainIndex) {
		this.content = content;
		this.nickname = nickname;
		this.itemId = itemId;
		this.imageUrls = imageUrls;
		this.mainIndex = mainIndex;
	}

	public static PostResponseDto from(Post post, ImageDto imageDto) {
		return new PostResponseDto(
			post.getContent(),
			post.getUser().getNickname(),
			post.getItem().getId(),
			imageDto.getImageUrls(),
			imageDto.getMainIndex());
	}
}