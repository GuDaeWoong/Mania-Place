package com.example.place.domain.Image.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.place.domain.Image.dto.ImageDto;
import com.example.place.domain.Image.entity.Image;
import com.example.place.domain.Image.repository.ImageRepository;
import com.example.place.domain.item.entity.Item;
import com.example.place.domain.newsfeed.entity.Newsfeed;
import com.example.place.domain.post.entity.Post;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService {

	private final ImageRepository imageRepository;

	// item의 이미지 저장
	@Transactional
	public ImageDto createImages(Item item, List<String> imageUrls, int mainIndex) {

		int validatedMainIndex = validMainIndex(imageUrls.size(), mainIndex);

		// 이미지 저장
		for (int i = 0; i < imageUrls.size(); i++) {
			boolean isMain = (validatedMainIndex == i);
			Image image = Image.of(item, imageUrls.get(i), isMain);

			imageRepository.save(image);
			item.addImage(image); // 연관관계 양방향 설정
		}

		return ImageDto.of(imageUrls, validatedMainIndex);
	}

	// newsfeed의 이미지 저장
	@Transactional
	public ImageDto createImages(Newsfeed newsfeed, List<String> imageUrls, int mainIndex) {

		int validatedMainIndex = validMainIndex(imageUrls.size(), mainIndex);

		for (int i = 0; i < imageUrls.size(); i++) {
			boolean isMain = (validatedMainIndex == i);
			Image image = Image.of(newsfeed, imageUrls.get(i), isMain);

			imageRepository.save(image);
			newsfeed.addImage(image);
		}

		return ImageDto.of(imageUrls, validatedMainIndex);
	}

	@Transactional(readOnly = true)
	public ImageDto getItemImages(Long itemId) {
		List<Image> images = imageRepository.findByItemId(itemId);

		List<String> imageUrls = new ArrayList<>();
		int mainIndex = 0;
		for (int i = 0; i < images.size(); i++) {
			imageUrls.add(images.get(i).getImageUrl());
			if (images.get(i).isMain()) {
				mainIndex = i;
			}
		}

		return ImageDto.of(imageUrls, mainIndex);
	}

	@Transactional(readOnly = true)
	public ImageDto getNewsfeedImages(Long newsfeedId) {
		List<Image> images = imageRepository.findByNewsfeedId(newsfeedId);

		List<String> imageUrls = new ArrayList<>();
		int mainIndex = 0;
		for (int i = 0; i < images.size(); i++) {
			imageUrls.add(images.get(i).getImageUrl());
			if (images.get(i).isMain()) {
				mainIndex = i;
			}
		}

		return ImageDto.of(imageUrls, mainIndex);
	}

	// item의 이미지 수정
	@Transactional
	public ImageDto updateImages(Item item, List<String> newImageUrls, int mainIndex) {
		return updateImagesCommon(item, null, newImageUrls, mainIndex);
	}

	// Newsfeed 이미지 수정
	@Transactional
	public ImageDto updateImages(Newsfeed newsfeed, List<String> newImageUrls, Integer mainIndex) {
		int validMainIndex = (mainIndex != null) ? mainIndex : 0;
		return updateImagesCommon(null, newsfeed, newImageUrls, validMainIndex);
	}

	@Transactional
	public ImageDto updateImagesCommon(Item item, Newsfeed newsfeed, List<String> newImageUrls, int mainIndex) {
		// 새롭게 전달받은 이미지
		Set<String> newImageSet = new HashSet<>(newImageUrls);

		// 현재 저장되어 있는 이미지
		List<Image> existingImages = (item != null)
			? imageRepository.findByItemId(item.getId())
			: imageRepository.findByNewsfeedId(newsfeed.getId());

		Set<String> existingImageSet = existingImages.stream()
			.map(Image::getImageUrl)
			.collect(Collectors.toSet());

		// 기존의 삭제할 이미지 판별 	(existing - new)
		Set<String> toDelete = new HashSet<>(existingImageSet);
		toDelete.removeAll(newImageSet);

		// 삭제 처리
		for (Image image : existingImages) {
			if (toDelete.contains(image.getImageUrl())) {
				imageRepository.delete(image);
			}
		}

		// 새롭게 저장할 이미지 판별 	(new - existing)
		Set<String> toSave = new HashSet<>(newImageSet);
		toSave.removeAll(existingImageSet);

		// 추가 처리(+Item/Newsfeed 구분해서)
		for (String imageUrl : toSave) {
			Image image = (item != null)
				? Image.of(item, imageUrl, false)
				: Image.of(newsfeed, imageUrl, false);  // 임시로 대표 이미지 false로 세팅
			imageRepository.save(image);
		}

		// 대표 이미지 재설정 및 반환값 담기
		int validatedMainIndex = validMainIndex(newImageUrls.size(), mainIndex);
		String mainImageUrl = newImageUrls.get(validatedMainIndex);
		// Item/Newsfeed 구분해서 조회
		List<Image> images = (item != null)
			? imageRepository.findByItemId(item.getId())
			: imageRepository.findByNewsfeedId(newsfeed.getId());

		List<String> resultImageUrls = new ArrayList<>();
		int resultMainIndex = 0;

		for (int i = 0; i < images.size(); i++) {
			Image image = images.get(i);

			if (image.getImageUrl().equals(mainImageUrl)) {
				image.updateIsMain(true); // 대표 이미지 여부 재설정
				resultMainIndex = i;      // 대표 이미지 인덱스 반환을 위해
			} else {
				image.updateIsMain(false);
			}

			resultImageUrls.add(image.getImageUrl());
		}

		return ImageDto.of(resultImageUrls, resultMainIndex);
	}

	// 특정 itemId와 연관된 이미지를 일괄로 삭제
	@Transactional
	public void deleteImageByItemId(Long itemId) {
		// 연관 이미지들 삭제
		List<Image> images = imageRepository.findByItemId(itemId);
		imageRepository.deleteAll(images);
	}

	//특정 NewsfeedId와 연관된 이미지를 일괄로 삭제
	@Transactional
	public void deleteImageByNewsfeedId(Long newsfeedId) {
		List<Image> images = imageRepository.findByNewsfeedId(newsfeedId);
		imageRepository.deleteAll(images);
	}

	// 대표 이미지 인덱스 검증
	private int validMainIndex(int listSize, int mainIndex) {

		return (mainIndex < 0 || mainIndex >= listSize) ? 0 : mainIndex;
	}

	public String getMainImageUrl(Long itemId) {

		return imageRepository.findByItemIdAndIsMainTrue(itemId)
			.map(Image::getImageUrl)
			.orElse(null);
	}

	// 현재 상품 페이지에 있는 상품의 이미지들을 맵으로 묶어 반환
	@Transactional(readOnly = true)
	public Map<Long, Image> getMainImagesForItems(Page<Item> pagedItems) {
		// 현재 페이지에 존재하는 itemId 리스트
		List<Long> itemIds = pagedItems.getContent().stream()
			.map(Item::getId)
			.distinct()
			.collect(Collectors.toList());

		if (itemIds.isEmpty()) {
			return Collections.emptyMap();
		}

		// 해당 상품들의 대표 이미지 조회
		List<Image> mainImages = imageRepository.findMainImagesByItemIds(itemIds);

		// 결과 리스트를 itemId를 키로 하는 Map으로 변환
		return mainImages.stream()
			.collect(Collectors.toMap(
				img -> img.getItem().getId(),
				img -> img
			));
	}

	// 현재 게시글 페이지에 있는 상품의 이미지들을 맵으로 묶어 반환
	@Transactional(readOnly = true)
	public Map<Long, Image> getMainImagesForPosts(Page<Post> pagedPosts) {
		// 현재 페이지에 존재하는 itemId 리스트
		List<Long> itemIds = pagedPosts.getContent().stream()
			.map(post -> post.getItem().getId())
			.distinct()
			.collect(Collectors.toList());

		if (itemIds.isEmpty()) {
			return Collections.emptyMap();
		}

		// 해당 게시글들의 대표 이미지 조회
		List<Image> mainImages = imageRepository.findMainImagesByItemIds(itemIds);

		// 결과 리스트를 itemId를 키로 하는 Map으로 변환
		return mainImages.stream()
			.collect(Collectors.toMap(
				img -> img.getItem().getId(),
				img -> img
			));
	}

	// 현재 새소식 페이지에 있는 상품의 이미지들을 맵으로 묶어 반환
	@Transactional(readOnly = true)
	public Map<Long, Image> getMainImagesForNewsfeeds(Page<Newsfeed> pagedNewsfeeds) {
		// 현재 페이지에 존재하는 newsfeedId 리스트
		List<Long> newsfeedIds = pagedNewsfeeds.getContent().stream()
			.map(Newsfeed::getId)
			.collect(Collectors.toList());

		if (newsfeedIds.isEmpty()) {
			return Collections.emptyMap();
		}

		// 해당 새소식의 대표 이미지 조회
		List<Image> mainImages = imageRepository.findMainImagesByNewsfeedIds(newsfeedIds);

		// 결과 리스트를 newsfeedId를 키로 하는 Map으로 변환
		return mainImages.stream()
			.collect(Collectors.toMap(
				image -> image.getNewsfeed().getId(),
				img -> img
			));
	}

}
