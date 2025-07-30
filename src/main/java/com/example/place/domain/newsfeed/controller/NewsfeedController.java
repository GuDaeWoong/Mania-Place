package com.example.place.domain.newsfeed.controller;

import com.example.place.common.dto.ApiResponseDto;
import com.example.place.common.security.jwt.CustomPrincipal;
import com.example.place.domain.newsfeed.dto.request.NewsfeedRequest;
import com.example.place.domain.newsfeed.dto.response.NewsfeedResponse;
import com.example.place.domain.newsfeed.service.NewsfeedService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/newsfeeds")
public class NewsfeedController {

	private final NewsfeedService newsfeedService;

	@PostMapping
	public ResponseEntity<ApiResponseDto<NewsfeedResponse>> createNewsfeed(
		@Valid @RequestBody NewsfeedRequest request,
		@AuthenticationPrincipal CustomPrincipal principal
	) {
		NewsfeedResponse newsfeed = newsfeedService.createNewsfeed(principal.getId(), request);
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.of("게시글 등록이 완료되었습니다.", newsfeed));
	}

}
