package com.example.place.domain.Image.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.example.place.common.annotation.Loggable;
import com.example.place.domain.Image.entity.Image;
import com.example.place.domain.Image.repository.ImageRepository;
import com.example.place.domain.item.entity.Item;
import com.example.place.domain.newsfeed.entity.Newsfeed;
import com.example.place.domain.post.entity.Post;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService {

	private final ImageRepository imageRepository;

	// 이미지 저장
	@Transactional
	public void createImages(Item item, List<String> imageUrls, int mainIndex) {

		int validatedMainIndex = validMainIndex(imageUrls.size(), mainIndex);

		// 이미지 저장
		for (int i = 0; i < imageUrls.size(); i++) {
			boolean isMain = (validatedMainIndex == i);
			Image image = Image.of(item, imageUrls.get(i), isMain);

			imageRepository.save(image);

			item.addImage(image); // 연관관계 양방향 설정
		}
	}

	// newsfeed 의 이미지 저장
	@Transactional
	public void createImages(Newsfeed newsfeed, List<String> imageUrls, int mainIndex) {

		int validatedMainIndex = validMainIndex(imageUrls.size(), mainIndex);

		for (int i = 0; i < imageUrls.size(); i++) {
			boolean isMain = (validatedMainIndex == i);
			Image image = Image.of(newsfeed, imageUrls.get(i), isMain);

			imageRepository.save(image);
			newsfeed.addImage(image);
		}
	}

	// 이미지 수정
	@Transactional
	public void updateImages(Item item, List<String> newImageUrls, int mainIndex) {
		// 새롭게 전달받은 이미지
		Set<String> newImageSet = new HashSet<>(newImageUrls);

		// 현재 저장되어 있는 이미지
		List<Image> existingImages = imageRepository.findByItemId(item.getId());
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

		// 추가 처리
		for (String imageUrl : toSave) {
			Image image = Image.of(item, imageUrl, false);  // 임시로 대표 이미지 false로 세팅
			imageRepository.save(image);
		}

		// 대표 이미지 재설정
		int validatedMainIndex = validMainIndex(newImageUrls.size(), mainIndex);
		String mainImageUrl = newImageUrls.get(validatedMainIndex);
		List<Image> images = imageRepository.findByItemId(item.getId());
		for (Image image : images) {
			image.updateIsMain(image.getImageUrl().equals(mainImageUrl));
		}
	}

	// 특정 itemId와 연관된 이미지를 일괄로 삭제
	@Transactional
	public void deleteImageByItemId(Long itemId) {
		// 연관 이미지들 삭제
		List<Image> images = imageRepository.findByItemId(itemId);
		imageRepository.deleteAll(images);
	}

	// //특정 NewsfeedId와 연관된 이미지를 일괄로 삭제
	// @Transactional
	// public void deleteImageByNewsfeedId(Long newsfeedId) {
	// 	List<Image> images = imageRepository.findByNewsfeedId(newsfeedId);
	// 	imageRepository.deleteAll(images);
	// }

	// 대표 이미지 인덱스 검증
	private int validMainIndex(int listSize, int mainIndex) {

		return (mainIndex < 0 || mainIndex >= listSize) ? 0 : mainIndex;
	}

	@Transactional
	public List<Image> findByItemIds(List<Long> itemIds) {
		return imageRepository.findByItemIds(itemIds);
	}

	// 현재 페이지에 있는 상품의 이미지들을 맵으로 묶어 반환
	public Map<Long, List<Image>> mapItemIdsToImagesFromPosts(Page<Post> postsPage) {
		// 현재 페이지에 존재하는 상품 리스트
		List<Long> itemIds = postsPage.stream()
			.map(post -> post.getItem().getId())
			.distinct()
			.collect(Collectors.toList());

		// 해당 상품들의 이미지들 조회
		List<Image> images = findByItemIds(itemIds);

		// 상품별로 이미지 리스트 생성
		Map<Long, List<Image>> itemIdToImagesMap = images.stream()
			.collect(Collectors.groupingBy(img -> img.getItem().getId()));

		return itemIdToImagesMap;
	}
}
