package com.example.place.domain.newsfeed.controller;

import com.example.place.common.dto.ApiResponseDto;
import com.example.place.common.dto.PageResponseDto;
import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;
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
		// Admin 권한 확인
		checkAdminRole(principal);

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

	//새소식 수정
	@PatchMapping("/{newsfeedId}")
	public ResponseEntity<ApiResponseDto<NewsfeedResponse>> updateNewsfeed(
		@PathVariable Long newsfeedId,
		@RequestBody NewsfeedRequest request,
		@AuthenticationPrincipal CustomPrincipal principal
	) {

		// Admin 권한 확인
		checkAdminRole(principal);

		NewsfeedResponse updateNewsfeed = newsfeedService.updateNewsfeed(newsfeedId, request, principal.getId());
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.of("게시글 수정이 완료되었습니다.", updateNewsfeed));
	}

	//새소식 삭제
	@DeleteMapping("/{newsfeedId}")
	public ResponseEntity<ApiResponseDto<Void>> softDeleteNewsfeed(
		@PathVariable Long newsfeedId,
		@AuthenticationPrincipal CustomPrincipal principal
	) {

		// Admin 권한 확인
		checkAdminRole(principal);

		newsfeedService.softDeleteNewsfeed(newsfeedId, principal.getId());
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.of("게시글 삭제가 완료되었습니다.", null));
	}

	// Admin 권한 확인
	private void checkAdminRole(CustomPrincipal principal) {
		// authorities를 사용
		boolean isAdmin = principal.getAuthorities().stream()
			.anyMatch(auth -> "ROLE_ADMIN".equals(auth.getAuthority()) ||
				"ADMIN".equals(auth.getAuthority()));

		if (!isAdmin) {
			throw new CustomException(ExceptionCode.FORBIDDEN_ADMIN_ACCESS);
		}
	}
}
