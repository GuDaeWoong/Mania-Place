package com.example.place.domain.postcomment.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.place.common.dto.ApiResponseDto;
import com.example.place.common.security.jwt.CustomPrincipal;
import com.example.place.domain.postcomment.dto.PostCommentRequest;
import com.example.place.domain.postcomment.dto.PostCommentResponse;
import com.example.place.domain.postcomment.service.PostCommnetService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/post")
public class PostCommentController {

	private final PostCommnetService postCommnetService;

	@PostMapping("/{postId}/comments")
	public ResponseEntity<ApiResponseDto<PostCommentResponse>> savePostComment(
		@PathVariable Long postId,
		@Valid @RequestBody PostCommentRequest request,
		@AuthenticationPrincipal CustomPrincipal principal) {

		PostCommentResponse response = postCommnetService.savePostComment(postId, request, principal);

		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<>("댓글이 등록되었습니다.", response));
	}

	@PutMapping("/comments/{commentId}")
	public ResponseEntity<ApiResponseDto<PostCommentResponse>> updatePostComment(
		@PathVariable Long commentId,
		@Valid @RequestBody PostCommentRequest request,
		@AuthenticationPrincipal CustomPrincipal principal
	) {
		PostCommentResponse response = postCommnetService.updatePostComment(commentId, request, principal);

		return ResponseEntity.ok(new ApiResponseDto<>("게시글이 수정이 완료되었습니다.", response));
	}

	@GetMapping("/{postId}/comments")
	public ResponseEntity<ApiResponseDto<Page<PostCommentResponse>>> getCommentsByPost(
		@PathVariable Long postId,
		@PageableDefault Pageable pageable

	) {
		Page<PostCommentResponse> responses = postCommnetService.getCommentsByPost(postId, pageable);
		return ResponseEntity.ok(new ApiResponseDto<>("댓글 목록 조회 완료", responses));
	}


}
