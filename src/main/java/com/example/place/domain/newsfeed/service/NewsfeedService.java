package com.example.place.domain.newsfeed.service;

import java.util.Map;

import com.example.place.common.annotation.Loggable;
import com.example.place.common.dto.PageResponseDto;
import com.example.place.domain.Image.dto.ImageDto;
import com.example.place.domain.Image.entity.Image;
import com.example.place.domain.Image.service.ImageService;
import com.example.place.domain.newsfeed.dto.request.NewsfeedRequest;
import com.example.place.domain.newsfeed.dto.response.NewsfeedListResponse;
import com.example.place.domain.newsfeed.dto.response.NewsfeedResponse;
import com.example.place.domain.newsfeed.entity.Newsfeed;
import com.example.place.domain.newsfeed.repository.NewsfeedRepository;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.user.service.UserService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;

import lombok.RequiredArgsConstructor;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NewsfeedService {

	private final NewsfeedRepository newsfeedRepository;
	private final UserService userService;
	private final ImageService imageService;
	private final NewsfeedCacheService cacheService;

	@Loggable
	@Transactional
	public NewsfeedResponse createNewsfeed(Long userId, NewsfeedRequest request) {
		User user = userService.findByIdOrElseThrow(userId);

		Newsfeed newsfeed = Newsfeed.of(
			user,
			request.getTitle(),
			request.getContent()
		);
		Newsfeed savedNewsfeed = newsfeedRepository.save(newsfeed);
		// 이미지 저장
		ImageDto imageDto = imageService.createImages(savedNewsfeed, request.getImageUrls(), request.getMainIndex());

		NewsfeedResponse response = NewsfeedResponse.from(savedNewsfeed, imageDto);

		//캐시 무효화
		cacheService.evictNewsfeedListCaches();

		return NewsfeedResponse.from(savedNewsfeed, imageDto);
	}

	@Loggable
	@Transactional(readOnly = true)
	public NewsfeedResponse getNewsfeed(Long newsfeedId) {
		// 캐시에서 먼저 조회
		NewsfeedResponse cachedResponse = cacheService.getNewsfeed(newsfeedId);
		if (cachedResponse != null) {
			return cachedResponse;
		}

		//기존 로직(캐시에 없으니 DB에서 가져오기)
		Newsfeed newsfeed = findByIdOrElseThrow(newsfeedId);
		ImageDto imageDto = imageService.getNewsfeedImages(newsfeedId);
		NewsfeedResponse response = NewsfeedResponse.from(newsfeed, imageDto);

		//조회 결과 캐시에 저장
		cacheService.putNewsfeed(newsfeedId, response);

		return response;
	}

	@Loggable
	@Transactional(readOnly = true)
	public PageResponseDto<NewsfeedListResponse> getAllNewsfeeds(Pageable pageable) {

		//캐시에서 먼저 조회
		PageResponseDto<NewsfeedListResponse> cachedResponse = cacheService.getNewsfeedList(pageable);
		if (cachedResponse != null) {
			return cachedResponse;
		}

		//기존 로직(캐시에 없으니 DB에서 가져오기)
		// 새소식 전체 조회(소프트딜리트 빼고)
		Page<Newsfeed> pagedNewsfeeds = newsfeedRepository.findByIsDeletedFalseWithFetchJoin(pageable);

		// 페이지에 들어갈 대표 이미지 일괄 조회
		Map<Long, Image> mainImageMap = imageService.getMainImagesForNewsfeeds(pagedNewsfeeds);

		// NewsfeedListResponse 에 적용
		Page<NewsfeedListResponse> dtoPage = pagedNewsfeeds.map(newsfeed -> {
			// --메인이미지 조합
			Image mainImage = mainImageMap.getOrDefault(newsfeed.getId(), null);

			return NewsfeedListResponse.of(newsfeed, mainImage.getImageUrl());
		});

		PageResponseDto<NewsfeedListResponse> response = new PageResponseDto<>(dtoPage);

		cacheService.putNewsfeedList(pageable, response);

		return response;
	}

	public Newsfeed findByIdOrElseThrow(Long id) {
		Newsfeed newsfeed = newsfeedRepository.findById(id)
			.orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_NEWSFEED));

		if (newsfeed.isDeleted()) {
			throw new CustomException(ExceptionCode.NOT_FOUND_NEWSFEED);
		}

		return newsfeed;
	}

	// 새소식 수정
	@Transactional
	public NewsfeedResponse updateNewsfeed(Long newsfeedId, NewsfeedRequest request, Long userId) {
		Newsfeed newsfeed = findByIdOrElseThrow(newsfeedId);

		if (!newsfeed.getUser().getId().equals(userId)) {
			throw new CustomException(ExceptionCode.FORBIDDEN_NEWSFEED_ACCESS);
		}
		newsfeed.updateNewsfeed(request);

		// 이미지 수정
		if (((request.getImageUrls() == null || request.getImageUrls().isEmpty()) && request.getMainIndex() != null)
			|| (request.getImageUrls() != null && request.getMainIndex() == null)) {
			throw new CustomException(ExceptionCode.INVALID_IMAGE_UPDATE_REQUEST);
		}
		ImageDto imageDto = (request.getImageUrls() != null)
			? imageService.updateImages(newsfeed, request.getImageUrls(), request.getMainIndex())
			: imageService.getNewsfeedImages(newsfeedId);

		NewsfeedResponse response = NewsfeedResponse.from(newsfeed, imageDto);

		// 수정 시 단건 조회 캐시, 전체 조회 캐시 무효화
		cacheService.evictNewsfeed(newsfeedId);
		cacheService.evictNewsfeedListCaches();

		return response;
	}

	@Loggable
	@Transactional
	public void softDeleteNewsfeed(Long newsfeedId, Long userId) {
		Newsfeed newsfeed = findByIdOrElseThrow(newsfeedId);

		if (!findByIdOrElseThrow(newsfeedId).getUser().getId().equals(userId)) {
			throw new CustomException(ExceptionCode.FORBIDDEN_NEWSFEED_DELETE);
		}

		newsfeed.delete();

		// 삭제 시 단건 조회 캐시, 전체 조회 캐시 무효화
		cacheService.evictNewsfeed(newsfeedId);
		cacheService.evictNewsfeedListCaches();
	}

}

