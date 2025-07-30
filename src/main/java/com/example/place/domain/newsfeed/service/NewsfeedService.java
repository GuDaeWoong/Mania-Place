package com.example.place.domain.newsfeed.service;

import com.example.place.domain.Image.service.ImageService;
import com.example.place.domain.Image.entity.Image;
import com.example.place.domain.newsfeed.dto.request.NewsfeedRequest;
import com.example.place.domain.newsfeed.dto.response.NewsfeedResponse;
import com.example.place.domain.newsfeed.entity.Newsfeed;
import com.example.place.domain.newsfeed.repository.NewsfeedRepository;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.user.service.UserService;

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

	@Transactional
	public NewsfeedResponse createNewsfeed(Long userId, NewsfeedRequest request) {
		User user = userService.findByIdOrElseThrow(userId);

		Newsfeed newsfeed = Newsfeed.of(
			user,
			request.getNewsfeedTitle(),
			request.getNewsfeedContent()
		);
		Newsfeed savedNewsfeed = newsfeedRepository.save(newsfeed);
		// 이미지 저장
		imageService.createImages(savedNewsfeed, request.getImageUrls(), request.getMainIndex());

		return NewsfeedResponse.from(savedNewsfeed);
	}

	public String getMainImageUrl(Long newsfeedId) {
		Newsfeed newsfeed = newsfeedRepository.findById(newsfeedId)
			.orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_NEWSFEED));

		return newsfeed.getImages().stream()
			.filter(Image::isMain)
			.findFirst()
			.map(Image::getImageUrl)
			.orElse(null);
	}
}

