package com.example.place.domain.newsfeed.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.place.common.annotation.Loggable;
import com.example.place.common.dto.PageResponseDto;
import com.example.place.domain.Image.repository.ImageRepository;
import com.example.place.domain.Image.service.ImageService;
import com.example.place.domain.Image.entity.Image;
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
	private final ImageRepository imageRepository;
	private final UserService userService;
	private final ImageService imageService;

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
		imageService.createImages(savedNewsfeed, request.getImageUrls(), request.getMainIndex());

		return NewsfeedResponse.from(savedNewsfeed);
	}

	@Loggable
	@Transactional(readOnly = true)
	public NewsfeedResponse getNewsfeed(Long newsfeedId) {
		Newsfeed newsfeed = findByIdOrElseThrow(newsfeedId);
		return NewsfeedResponse.from(newsfeed);
	}

	@Loggable
	@Transactional(readOnly = true)
	public PageResponseDto<NewsfeedListResponse> getAllNewsfeeds(Pageable pageable) {

		// 새소식 전체 조회(소프트딜리트 빼고)
		Page<Newsfeed> pagedNewsfeeds = newsfeedRepository.findByIsDeletedFalse(pageable);

		// 페이지에 들어갈 새소식 ID 추출
		List<Long> newsfeedIds = pagedNewsfeeds.getContent().stream()
			.map(Newsfeed::getId)
			.collect(Collectors.toList());

		// 페이지에 들어갈 대표 이미지 일괄 조회
		Map<Long, String> mainImageMap = getMainImageMap(newsfeedIds);

		// NewsfeedListResponse 에 적용
		Page<NewsfeedListResponse> response = pagedNewsfeeds.map(newsfeed ->
			NewsfeedListResponse.of(newsfeed, mainImageMap.get(newsfeed.getId()))
		);

		return new PageResponseDto<>(response);
	}

	public Newsfeed findByIdOrElseThrow(Long id) {
		Newsfeed newsfeed = newsfeedRepository.findById(id)
			.orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_NEWSFEED));

		if (newsfeed.isDeleted()) {
			throw new CustomException(ExceptionCode.NOT_FOUND_NEWSFEED);
		}

		return newsfeed;
	}

	// 대표 이미지 조회
	private Map<Long, String> getMainImageMap(List<Long> newsfeedIds) {
		//ID가 비었으면 바로 반환
		if (newsfeedIds.isEmpty()) {
			return Collections.emptyMap();
		}

		//대표 이미지만 조회
		List<Image> mainImages = imageRepository.findMainImagesByNewsfeedIds(newsfeedIds);

		//조회된 이미지 리스트를맵으로 변환
		return mainImages.stream()
			.collect(Collectors.toMap(
				image -> image.getNewsfeed().getId(),
				Image::getImageUrl
			));
	}
}

