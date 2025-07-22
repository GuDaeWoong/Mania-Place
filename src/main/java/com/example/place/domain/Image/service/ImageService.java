package com.example.place.domain.Image.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.place.domain.Image.entity.Image;
import com.example.place.domain.Image.repository.ImageRepository;
import com.example.place.domain.item.entity.Item;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService {

	private final ImageRepository imageRepository;

	// 이미지 저장
	@Transactional
	public void saveImage(Item item, String imageUrl, Boolean isMain) {
		Image image = Image.of(item, imageUrl, isMain);
		imageRepository.save(image);
	}

	// 특정 itemId와 연관된 이미지를 일괄로 삭제
	@Transactional
	public void deleteImageByItemId(Long itemId) {
		List<Image> images = imageRepository.findByItemId(itemId);
		imageRepository.deleteAll(images);
	}
}
