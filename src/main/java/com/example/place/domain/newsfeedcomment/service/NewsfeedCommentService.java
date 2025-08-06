package com.example.place.domain.newsfeedcomment.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.place.common.dto.PageResponseDto;
import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;
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

	//댓글 조회
	@Transactional
	public PageResponseDto<NewsfeedCommentResponse> getAllCommentsByNewsfeeds(Long newsfeedId, Pageable pageable
	) {
		Newsfeed newsfeed = newsfeedService.findByIdOrElseThrow(newsfeedId);

		Page<NewsfeedComment> commentPage = newsfeedCommentRepository.findAllByNewsfeedAndIsDeletedFalse(newsfeed,
			pageable);

		Page<NewsfeedCommentResponse> responsePage = commentPage.map(
			comment -> NewsfeedCommentResponse.from(comment.getUser(), comment));
		return new PageResponseDto<>(responsePage);
	}

	//댓글 수정
	@Transactional
	public NewsfeedCommentResponse updateNewsfeedComment(Long newsfeedId, Long commentId,
		NewsfeedCommentRequest request, CustomPrincipal principal) {

		newsfeedService.findByIdOrElseThrow(newsfeedId);

		User user = userService.findByIdOrElseThrow(principal.getId());

		NewsfeedComment newsfeedComment = newsfeedCommentRepository.findByIdAndIsDeletedFalse(commentId)
			.orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_COMMENT));

		if (!newsfeedComment.getUser().getId().equals(user.getId())) {
			throw new CustomException(ExceptionCode.FORBIDDEN_POST_ACCESS);
		}

		newsfeedComment.updateContent(request.getContent());

		return NewsfeedCommentResponse.from(user, newsfeedComment);
	}
}
