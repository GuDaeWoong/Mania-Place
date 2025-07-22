package com.example.place.domain.post.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.place.common.exception.exceptionclass.CustomException;
import com.example.place.domain.post.dto.PostCreateRequestDto;
import com.example.place.domain.post.dto.PostResponseDto;
import com.example.place.domain.post.entity.Post;
import com.example.place.domain.post.service.PostService;
import com.example.place.domain.user.entity.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

	private final PostService postService;

	public PostResponseDto createPost(PostCreateRequestDto request, CustomPrincipal principal) {
		User user = userRepository.findById(principal.getUserId())
			.orElseThrow(() -> new CustomException(USER_NOT_FOUND));

		Post post = new Post(request.getContent(), request.getImage(), user, item);

		Post saved = postRepository.save(post);

		return new PostResponseDto(saved);
	}

	@GetMapping("/{postId}")

	@GetMapping
	public ResponseEntity<Page<PostResponseDto>> getAllPosts(Pageable pageable) {
		Page<PostResponseDto> response = postService.findAllPosts(pageable);
		return ResponseEntity.ok(response);
	}

}
