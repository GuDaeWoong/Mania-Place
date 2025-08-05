package com.example.place.domain.newsfeedcomment.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.place.common.dto.ApiResponseDto;
import com.example.place.common.dto.PageResponseDto;
import com.example.place.common.security.jwt.CustomPrincipal;
import com.example.place.domain.newsfeed.entity.Newsfeed;
import com.example.place.domain.newsfeedcomment.dto.request.NewsfeedCommentRequest;
import com.example.place.domain.newsfeedcomment.dto.response.NewsfeedCommentResponse;
import com.example.place.domain.newsfeedcomment.entity.NewsfeedComment;
import com.example.place.domain.newsfeedcomment.service.NewsfeedCommentService;
import com.example.place.domain.post.entity.Post;
import com.example.place.domain.postcomment.dto.response.PostCommentResponseDto;
import com.example.place.domain.postcomment.entity.PostComment;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/newsfeeds")
public class NewsfeedCommentController {

	private final NewsfeedCommentService newsfeedCommentService;

	@PostMapping("/{newsfeedId}/comments")
	public ResponseEntity<ApiResponseDto<NewsfeedCommentResponse>> createNewsfeedComment(
		@PathVariable Long newsfeedId,
		@Valid @RequestBody NewsfeedCommentRequest request,
		@AuthenticationPrincipal CustomPrincipal principal) {

		NewsfeedCommentResponse response = newsfeedCommentService.createNewsfeedComment(newsfeedId, request, principal);
		ApiResponseDto<NewsfeedCommentResponse> success = ApiResponseDto.of("댓글이 등록되었습니다.", response);
		return ResponseEntity.status(HttpStatus.OK).body(success);
	}

	@GetMapping("/{newsfeedId}/comments")
	public ResponseEntity<ApiResponseDto<PageResponseDto>> getAllCommentsByNewsfeeds(
		@PathVariable Long newsfeedId,
		@PageableDefault Pageable pageable
	) {
		PageResponseDto<NewsfeedCommentResponse> response = newsfeedCommentService.getAllCommentsByNewsfeeds(newsfeedId,
			pageable);
		ApiResponseDto<PageResponseDto> success = ApiResponseDto.of("댓글 조회가 완료되었습니다.", response);
		return ResponseEntity.status(HttpStatus.OK).body(success);
	}

	//댓글 수정
	@PatchMapping("{newsfeedId}/comments/{commentId}")
	public ResponseEntity<ApiResponseDto<NewsfeedCommentResponse>> updateNewsfeedComment(
		@PathVariable Long newsfeedId,
		@PathVariable Long commentId,
		@Valid @RequestBody NewsfeedCommentRequest request,
		@AuthenticationPrincipal CustomPrincipal principal
	) {
		NewsfeedCommentResponse response = newsfeedCommentService.updateNewsfeedComment(newsfeedId, commentId, request,
			principal);
		ApiResponseDto<NewsfeedCommentResponse> success = ApiResponseDto.of("댓글이 수정이 완료되었습니다.", response);
		return ResponseEntity.status(HttpStatus.OK).body(success);
	}
}
