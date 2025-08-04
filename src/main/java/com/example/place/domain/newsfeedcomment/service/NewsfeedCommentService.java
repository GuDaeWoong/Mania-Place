package com.example.place.domain.newsfeedcomment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.place.common.security.jwt.CustomPrincipal;
import com.example.place.domain.newsfeed.entity.Newsfeed;
import com.example.place.domain.newsfeed.service.NewsfeedService;
import com.example.place.domain.newsfeedcomment.dto.request.NewsfeedCommentRequest;
import com.example.place.domain.newsfeedcomment.dto.response.NewsfeedCommentResponse;
import com.example.place.domain.newsfeedcomment.entity.NewsfeedComment;
import com.example.place.domain.newsfeedcomment.repository.NewsfeedCommentRepository;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NewsfeedCommentService {

	private final NewsfeedService newsfeedService;
	private final UserService userService;
	private final NewsfeedCommentRepository newsfeedCommentRepository;

	//댓글 생성
	@Transactional
	public NewsfeedCommentResponse createNewsfeedComment(Long newsfeedId, NewsfeedCommentRequest request,
		CustomPrincipal principal) {
		User user = userService.findByIdOrElseThrow(principal.getId());

		Newsfeed newsfeed = newsfeedService.findByIdOrElseThrow(newsfeedId);

		NewsfeedComment newsfeedComment = NewsfeedComment.of(user, newsfeed, request.getContent());
		newsfeedCommentRepository.save(newsfeedComment);

		return NewsfeedCommentResponse.from(user, newsfeedComment);
	}

}
