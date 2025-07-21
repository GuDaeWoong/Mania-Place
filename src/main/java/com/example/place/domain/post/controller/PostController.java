package com.example.place.domain.post.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.place.domain.post.dto.PostCreateRequestDto;
import com.example.place.domain.post.dto.PostResponseDto;
import com.example.place.domain.post.service.PostService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

	private final PostService postService;

	@PostMapping
	public ResponseEntity<PostResponseDto> create(@RequestBody @Valid PostCreateRequestDto request) {
		PostResponseDto response = postService.createPost(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
