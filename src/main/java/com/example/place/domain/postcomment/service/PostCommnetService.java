package com.example.place.domain.postcomment.service;

import org.springframework.stereotype.Service;

import com.example.place.common.security.jwt.CustomPrincipal;
import com.example.place.domain.item.entity.Item;
import com.example.place.domain.post.entity.Post;
import com.example.place.domain.post.service.PostService;
import com.example.place.domain.postcomment.dto.PostCommentRequest;
import com.example.place.domain.postcomment.dto.PostCommentResponse;
import com.example.place.domain.postcomment.entity.PostComment;
import com.example.place.domain.postcomment.repository.PostCommentRepository;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostCommnetService {

	private final PostCommentRepository postCommentRepository;
	private final PostService postService;
	private final UserService userService;

	public PostCommentResponse savePostComment(Long postId, PostCommentRequest request, CustomPrincipal principal) {
		User user = userService.findUserById(principal.getId());

		Post post = postService.findByIdOrElseThrow(postId);

		PostComment postComment = PostComment.of(user, post, request.getContent());
		PostComment savePostComment = postCommentRepository.save(postComment);

		return new PostCommentResponse(
			user.getNickname(),
			user.getImageUrl(),
			postComment.getContent(),
			postComment.getUser().getCreatedAt()
		);

	}
}
