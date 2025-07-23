package com.example.place.domain.postcomment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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





}
