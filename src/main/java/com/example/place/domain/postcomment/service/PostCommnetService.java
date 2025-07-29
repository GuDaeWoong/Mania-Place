package com.example.place.domain.postcomment.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.place.common.dto.PageResponseDto;
import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;
import com.example.place.common.security.jwt.CustomPrincipal;
import com.example.place.domain.post.entity.Post;
import com.example.place.domain.post.service.PostService;
import com.example.place.domain.postcomment.dto.request.PostCommentRequestDto;
import com.example.place.domain.postcomment.dto.response.PostCommentResponseDto;
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

	@Transactional
	public PostCommentResponseDto createPostComment(Long postId, PostCommentRequestDto request, CustomPrincipal principal) {
		User user = userService.findByIdOrElseThrow(principal.getId());

		Post post = postService.findByIdOrElseThrow(postId);

		PostComment postComment = PostComment.of(user, post, request.getContent());
		postCommentRepository.save(postComment);

		return PostCommentResponseDto.from(user, postComment);
	}

	@Transactional
	public PostCommentResponseDto updatePostComment(Long postId ,Long commentId, PostCommentRequestDto request, CustomPrincipal principal) {

		postService.findByIdOrElseThrow(postId);

		User user = userService.findByIdOrElseThrow(principal.getId());

		PostComment postComment = postCommentRepository.findByIdAndIsDeletedFalse(commentId)
			.orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_COMMENT));

		// 작성자 검증
		if (!postComment.getUser().getId().equals(user.getId())) {
			throw new CustomException(ExceptionCode.FORBIDDEN_POST_ACCESS);
		}

		postComment.updateContent(request.getContent());

		return PostCommentResponseDto.from(user, postComment);
	}

	public PageResponseDto<PostCommentResponseDto> getAllCommentsByPosts(Long postId, Pageable pageable
	){
		Post post = postService.findByIdOrElseThrow(postId);

		Page<PostComment> commentPage = postCommentRepository.findAllByPostAndIsDeletedFalse(post, pageable);

		Page<PostCommentResponseDto> responsePage = commentPage.map(
			comment -> PostCommentResponseDto.from(comment.getUser(), comment));
		return new PageResponseDto<>(responsePage);
	}

	@Transactional
	public void deletePostComment(Long postId, Long commentId, Long userId) {

		postService.findByIdOrElseThrow(postId);

		PostComment comment = postCommentRepository.findById(commentId)
			.orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_COMMENT));

		if (!comment.getUser().getId().equals(userId)) {
			throw new CustomException(ExceptionCode.FORBIDDEN_COMMENT_ACCESS);
		}
		postCommentRepository.delete(comment);
	}

	@Transactional
	public void softDeletePostComment(Long postId, Long commentId, Long userId) {

		postService.findByIdOrElseThrow(postId);

		PostComment comment = postCommentRepository.findByIdAndIsDeletedFalse(commentId)
			.orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_COMMENT));

		if (!comment.getUser().getId().equals(userId)) {
			throw new CustomException(ExceptionCode.FORBIDDEN_COMMENT_ACCESS);
		}
		comment.delete();
	}

	@Transactional
	public void softDeleteAllPostComment(Long postId) {

		List<PostComment> comments = postCommentRepository.findByPostIdAndIsDeletedFalse(postId);

		for (PostComment comment : comments) {
			comment.delete();
		}
	}
}