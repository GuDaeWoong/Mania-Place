package com.example.place.domain.Image.service;

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

	@Transactional
	public void saveImage(Item item, String imageUrl, Boolean isMain) {
		Image image = Image.of(item, imageUrl, isMain);
		imageRepository.save(image);
	}
}
