package com.example.place.domain.post.service;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import com.example.place.domain.postcomment.service.PostCommnetService;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class PostDeleteService {

	private final PostService postService;
	private final PostCommnetService postCommnetService;

	@Transactional
	public void removeReferencesAndDeletePost(Long itemId, Long postId) {
		postService.softAllDeletePost(postId);
		postCommnetService.softDeleteAllPostComment(itemId);
	}
}