package com.example.place.domain.post.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.place.common.dto.ApiResponseDto;
import com.example.place.common.exception.exceptionclass.CustomException;
import com.example.place.domain.post.dto.request.PostCreateRequestDto;
import com.example.place.domain.post.dto.response.PostResponseDto;
import com.example.place.domain.post.dto.request.PostUpdateRequestDto;
import com.example.place.domain.post.service.PostService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;

	//살까말까 생성
	@PostMapping
	public ResponseEntity<ApiResponseDto<PostResponseDto>> createPost(
		@Valid @RequestBody PostCreateRequestDto request,
		@AuthenticationPrincipal CustomPrincipal principal
	) {
		PostResponseDto post = postService.createPost(principal.getId(), request);
		return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDto<>("게시글 등록이 완료되었습니다.", post));
	}

	//살까말까 단건 조회
	@GetMapping("/{postId}")
	public ResponseEntity<ApiResponseDto<PostResponseDto>> readPost(@PathVariable Long postId) {
		PostResponseDto post = postService.readPost(postId);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<>("게시글 조회가 완료되었습니다.", post));
	}

	//살까말까 전체 조회
	@GetMapping
	public ResponseEntity<ApiResponseDto<Page<PostResponseDto>>> getPosts(
		@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		Page<PostResponseDto> posts = postService.getAllPosts(pageable);
		return ResponseEntity.ok(ApiResponseDto.success(posts));
	}

	//살까말까 내 글 조회
	@GetMapping("/me")
	public ResponseEntity<ApiResponseDto<Page<PostResponseDto>>> getMyPosts(
		Pageable pageable,
		@AuthenticationPrincipal CustomPrincipal principal
	) {
		Page<PostResponseDto> response = postService.findMyPosts(principal.getId(), pageable);
		return ResponseEntity.ok(ApiResponseDto.success(response));
	}

	//살까말까 수정
	@PatchMapping("/{postId}")
	public ResponseEntity<ApiResponseDto<PostResponseDto>> updatePost(
		@PathVariable Long postId,
		@Valid @RequestBody PostUpdateRequestDto request,
		@AuthenticationPrincipal CustomPrincipal principal
	) {
		PostResponseDto updatePost = postService.updatePost(postId, request, principal.getId());
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<>("게시글 수정이 완료되었습니다.", updatePost));
	}

	//살까말까 삭제
	@DeleteMapping("/{postId}")
	public ResponseEntity<ApiResponseDto<Void>> deletePost(
		@PathVariable Long postId,
		@AuthenticationPrincipal CustomPrincipal principal
	) {
		postService.deletePost(postId, principal.getId());
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<>("게시글 삭제가 완료되었습니다.", null));
	}

}
