package com.example.place.domain.Image.dto;

import java.util.List;

import lombok.Getter;

@Getter
public class ImageDto {
	private List<String> imageUrls;
	private int mainIndex;

	private ImageDto(List<String> imageUrls, int mainIndex) {
		this.imageUrls = imageUrls;
		this.mainIndex = mainIndex;
	}

	public static ImageDto of(List<String> imageUrls, int mainIndex) {
		return new ImageDto(imageUrls, mainIndex);
	}
}
