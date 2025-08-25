package com.example.place.domain.newsfeed.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.place.common.annotation.Loggable;
import com.example.place.common.dto.PageResponseDto;
import com.example.place.domain.Image.dto.ImageDto;
import com.example.place.domain.Image.entity.Image;
import com.example.place.domain.Image.service.ImageService;
import com.example.place.domain.mail.service.MailRequestService;
import com.example.place.domain.newsfeed.dto.request.NewsfeedRequest;
import com.example.place.domain.newsfeed.dto.response.NewsfeedListResponse;
import com.example.place.domain.newsfeed.dto.response.NewsfeedResponse;
import com.example.place.domain.newsfeed.entity.Newsfeed;
import com.example.place.domain.newsfeed.repository.NewsfeedRepository;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.user.service.UserService;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
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
	private final MailRequestService mailRequestService;
	private final RedisTemplate<String, String> redisTemplate;

	// listCache 전체 삭제
	public void evictListCache() {
		ScanOptions options = ScanOptions.scanOptions().match("listCache::*").count(1000).build();
		try (Cursor<byte[]> cursor = redisTemplate.getConnectionFactory()
			.getConnection()
			.scan(options)) {
			while (cursor.hasNext()) {
				redisTemplate.delete(new String(cursor.next()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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

		// 새소식 메일
		mailRequestService.enqueueNewsfeedEmailToAllUsers(request.getTitle());

		// 캐시 전체 삭제
		evictListCache();

		return NewsfeedResponse.from(savedNewsfeed, imageDto);
	}

	@Loggable
	@Transactional(readOnly = true)
	@Cacheable(
		value = "singleCache",
		key = "#newsfeedId"
	)
	public NewsfeedResponse getNewsfeed(Long newsfeedId) {
		Newsfeed newsfeed = findByIdOrElseThrow(newsfeedId);

		ImageDto imageDto = imageService.getNewsfeedImages(newsfeedId);
		return NewsfeedResponse.from(newsfeed, imageDto);
	}

	@Loggable
	@Transactional(readOnly = true)
	@Cacheable(
		value = "listCache",
		key = "#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort.toString()"
	)
	public PageResponseDto<NewsfeedListResponse> getAllNewsfeeds(Pageable pageable) {

		// 새소식 전체 조회(소프트딜리트 빼고)
		Page<Newsfeed> pagedNewsfeeds = newsfeedRepository.findByIsDeletedFalseWithFetchJoin(pageable);

		// 페이지에 들어갈 대표 이미지 일괄 조회
		Map<Long, Image> mainImageMap = imageService.getMainImagesForNewsfeeds(pagedNewsfeeds);

		// NewsfeedListResponse 에 적용
		List<NewsfeedListResponse> contentList = pagedNewsfeeds.stream().map(newsfeed -> {
				Image mainImage = mainImageMap.getOrDefault(newsfeed.getId(), null);
				return NewsfeedListResponse.of(newsfeed, mainImage != null ? mainImage.getImageUrl() : null);
			})
			.collect(Collectors.toList()); // 가변 리스트로 변환

		return new PageResponseDto<>(
			new PageImpl<>(contentList, pageable, pagedNewsfeeds.getTotalElements())
		);
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
	@CacheEvict(value = "singleCache", key = "#newsfeedId")   // 단건 캐시 특정 키 삭제
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

		// 캐시 전체 삭제
		evictListCache();

		return NewsfeedResponse.from(newsfeed, imageDto);
	}

	// 새소식 삭제
	@Loggable
	@Transactional
	@CacheEvict(value = "singleCache", key = "#newsfeedId")   // 단건 캐시 특정 키 삭제
	public void softDeleteNewsfeed(Long newsfeedId, Long userId) {
		Newsfeed newsfeed = findByIdOrElseThrow(newsfeedId);

		if (!findByIdOrElseThrow(newsfeedId).getUser().getId().equals(userId)) {
			throw new CustomException(ExceptionCode.FORBIDDEN_NEWSFEED_DELETE);
		}

		// 캐시 전체 삭제
		evictListCache();

		newsfeed.delete();
	}

}