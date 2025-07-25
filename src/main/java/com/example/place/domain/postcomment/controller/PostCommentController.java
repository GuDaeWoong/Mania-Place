package com.example.place.domain.postcomment.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.place.common.dto.ApiResponseDto;
import com.example.place.common.dto.PageResponseDto;
import com.example.place.common.security.jwt.CustomPrincipal;
import com.example.place.domain.postcomment.dto.request.PostCommentRequestDto;
import com.example.place.domain.postcomment.dto.response.PostCommentResponseDto;
import com.example.place.domain.postcomment.service.PostCommnetService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/post")
public class PostCommentController {

	private final PostCommnetService postCommnetService;

	@PostMapping("/{postId}/comments")
	public ResponseEntity<ApiResponseDto<PostCommentResponseDto>> createPostComment(
		@PathVariable Long postId,
		@Valid @RequestBody PostCommentRequestDto request,
		@AuthenticationPrincipal CustomPrincipal principal) {

		PostCommentResponseDto response = postCommnetService.createPostComment(postId, request, principal);
		ApiResponseDto<PostCommentResponseDto> success = ApiResponseDto.of("댓글이 등록되었습니다.", response);
		return ResponseEntity.status(HttpStatus.OK).body(success);
	}

	@PatchMapping("{postId}/comments/{commentId}")
	public ResponseEntity<ApiResponseDto<PostCommentResponseDto>> updatePostComment(
		@PathVariable Long postId,
		@PathVariable Long commentId,
		@Valid @RequestBody PostCommentRequestDto request,
		@AuthenticationPrincipal CustomPrincipal principal
	) {
		PostCommentResponseDto response = postCommnetService.updatePostComment(postId, commentId, request, principal);
		ApiResponseDto<PostCommentResponseDto> success = ApiResponseDto.of("게시글이 수정이 완료되었습니다.", response);
		return ResponseEntity.status(HttpStatus.OK).body(success);
	}

	@GetMapping("/{postId}/comments")
	public ResponseEntity<ApiResponseDto<PageResponseDto>> getAllCommentsByPosts(
		@PathVariable Long postId,
		@PageableDefault Pageable pageable
	) {
		PageResponseDto<PostCommentResponseDto> response = postCommnetService.getAllCommentsByPosts(postId, pageable);
		ApiResponseDto<PageResponseDto> success = ApiResponseDto.of("댓글 목록 조회가 완료되었습니다.", response);
		return ResponseEntity.status(HttpStatus.OK).body(success);
	}

	@DeleteMapping("/{postId}/comments/{commentId}")
	public ResponseEntity<ApiResponseDto<Void>> deletePostComment(
		@PathVariable Long postId,
		@PathVariable Long commentId,
		@AuthenticationPrincipal CustomPrincipal principal
	) {
		Long userId = principal.getId();
		postCommnetService.deletePostComment(postId, commentId, userId);
		return ResponseEntity.ok(ApiResponseDto.of("댓글 삭제가 완료되었습니다.", null));
	}
}
