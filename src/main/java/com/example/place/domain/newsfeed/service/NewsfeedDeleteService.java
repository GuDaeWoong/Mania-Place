package com.example.place.domain.newsfeed.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.place.domain.newsfeedcomment.service.NewsfeedCommentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NewsfeedDeleteService {

	private final NewsfeedService newsfeedService;
	private final NewsfeedCommentService newsfeedCommentService;

	@Transactional
	public void removeReferencesAndDeleteNewsfeed(Long newsfeedId, Long userId) {
		newsfeedService.softDeleteNewsfeed(newsfeedId, userId);
		newsfeedCommentService.softDeleteAllNewsfeedComment(newsfeedId);
	}

}
