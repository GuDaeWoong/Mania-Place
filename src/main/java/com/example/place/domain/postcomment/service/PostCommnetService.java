package com.example.place.domain.postcomment.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;
import com.example.place.common.security.jwt.CustomPrincipal;
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
		postCommentRepository.save(postComment);

		return new PostCommentResponse(
			user.getNickname(),
			user.getImageUrl(),
			postComment.getContent(),
			postComment.getUser().getCreatedAt()
		);

	}

	@Transactional
	public PostCommentResponse updatePostComment(Long postId ,Long commentId, PostCommentRequest request, CustomPrincipal principal) {

		postService.findByIdOrElseThrow(postId);

		User user = userService.findUserById(principal.getId());

		PostComment postComment = postCommentRepository.findById(commentId)
			.orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_COMMENT));

		// 작성자 검증
		if (!postComment.getUser().getId().equals(user.getId())) {
			throw new CustomException(ExceptionCode.FORBIDDEN_POST_ACCESS);
		}

		postComment.updateContent(request.getContent());

		return new PostCommentResponse(
			user.getNickname(),
			user.getImageUrl(),
			postComment.getContent(),
			postComment.getUser().getCreatedAt()
		);
	}

	public Page<PostCommentResponse> getCommentsByPost(Long postId, Pageable pageable) {
		Post post = postService.findByIdOrElseThrow(postId);

		Page<PostComment> commentPage = postCommentRepository.findAllByPost(post, pageable);

		return commentPage.map(comment -> new PostCommentResponse(
			comment.getUser().getNickname(),
			comment.getUser().getImageUrl(),
			comment.getContent(),
			comment.getCreatedAt()
		));

	}
}
