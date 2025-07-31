package com.example.place.domain.newsfeed.controller;

import com.example.place.common.dto.ApiResponseDto;
import com.example.place.common.dto.PageResponseDto;
import com.example.place.common.security.jwt.CustomPrincipal;
import com.example.place.domain.newsfeed.dto.request.NewsfeedRequest;
import com.example.place.domain.newsfeed.dto.response.NewsfeedListResponse;
import com.example.place.domain.newsfeed.dto.response.NewsfeedResponse;
import com.example.place.domain.newsfeed.service.NewsfeedService;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

	//새소식 생성
	@PostMapping
	public ResponseEntity<ApiResponseDto<NewsfeedResponse>> createNewsfeed(
		@Valid @RequestBody NewsfeedRequest request,
		@AuthenticationPrincipal CustomPrincipal principal
	) {
		NewsfeedResponse newsfeed = newsfeedService.createNewsfeed(principal.getId(), request);
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.of("게시글 등록이 완료되었습니다.", newsfeed));
	}

	//단건 조회
	@GetMapping("/{newsfeedId}")
	public ResponseEntity<ApiResponseDto<NewsfeedResponse>> getNewsfeed(@PathVariable Long newsfeedId) {
		NewsfeedResponse newsfeed = newsfeedService.getNewsfeed(newsfeedId);
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.of("게시글 조회가 완료되었습니다.", newsfeed));
	}

	//전체 조회
	@GetMapping
	public ResponseEntity<ApiResponseDto<PageResponseDto<NewsfeedListResponse>>> getAllNewsfeeds(
		@PageableDefault Pageable pageable
	) {
		//리스트로 가져오기
		PageResponseDto<NewsfeedListResponse> response =
			newsfeedService.getAllNewsfeeds(pageable);
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.of("게시글 전체 조회가 완료되었습니다", response));
	}

}
