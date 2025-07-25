package com.example.place.domain.vote.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.example.place.common.dto.ApiResponseDto;
import com.example.place.common.security.jwt.CustomPrincipal;
import com.example.place.domain.vote.dto.request.VoteRequestDto;
import com.example.place.domain.vote.dto.response.VoteResponseDto;
import com.example.place.domain.vote.service.VoteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class VoteController {

	private final VoteService voteService;

	@PostMapping("/{postId}/vote")
	public ResponseEntity<ApiResponseDto<VoteResponseDto>> createVote(
		@PathVariable Long postId,
		@RequestBody @Valid VoteRequestDto request,
		@AuthenticationPrincipal CustomPrincipal principal
	) {
		
		Long userId = principal.getId();
		VoteResponseDto response = voteService.createVote(postId, request, userId);
		ApiResponseDto<VoteResponseDto> success = ApiResponseDto.of("투표가 반영되었습니다.", response);
		return ResponseEntity.status(HttpStatus.OK).body(success);
	}

	@DeleteMapping("/{postId}/vote")
	public ResponseEntity<ApiResponseDto<VoteResponseDto>> deleteVote(
		@PathVariable Long postId,
		@RequestBody @Valid VoteRequestDto request,
		@AuthenticationPrincipal CustomPrincipal principal
	) {
		Long userId = principal.getId();
		VoteResponseDto response = voteService.deleteVote(postId, request, userId);
		ApiResponseDto<VoteResponseDto> success = ApiResponseDto.of("투표가 취소되었습니다.", response);
		return ResponseEntity.status(HttpStatus.OK).body(success);
	}
}
