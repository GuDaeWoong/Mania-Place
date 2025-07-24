package com.example.place.domain.itemcomment.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.place.common.dto.PageResponseDto;
import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;
import com.example.place.common.security.jwt.CustomPrincipal;
import com.example.place.domain.item.entity.Item;
import com.example.place.domain.item.service.ItemService;
import com.example.place.domain.itemcomment.dto.request.ItemCommentRequest;
import com.example.place.domain.itemcomment.dto.response.ItemCommentResponse;
import com.example.place.domain.itemcomment.entity.ItemComment;
import com.example.place.domain.itemcomment.repository.ItemCommentRepository;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.user.service.UserService;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemCommentService {

	private final ItemCommentRepository itemCommentRepository;
	private final ItemService itemService;
	private final UserService userService;

	// 상품 댓글 저장
	@Transactional
	public ItemCommentResponse saveItemComment(Long itemId, ItemCommentRequest request,
		CustomPrincipal principal) {
		// 로그인 유저 조회
		User user = userService.findUserById(principal.getId());

		// 대상 상품 조회
		Item item = itemService.findByIdOrElseThrow(itemId);

		// 댓글 저장
		ItemComment comment = ItemComment.of(user, item, request.getContent());
		ItemComment saveComment = itemCommentRepository.save(comment);

		// 응답 DTO로 반환
		return ItemCommentResponse.from(saveComment);
	}

	// 상품 댓글 조회
	@Transactional(readOnly = true)
	public PageResponseDto<ItemCommentResponse> readItemComment(Long itemId, Pageable pageable) {
		// 해당 상품이 존재하는지 여부 확인
		itemService.findByIdOrElseThrow(itemId);

		// 댓글 조회
		Page<ItemComment> comments = itemCommentRepository.findByItemId(itemId, pageable);

		Page<ItemCommentResponse> ItemCommentPage = comments.map(ItemCommentResponse::from);

		// 응답 DTO로 반환
		return new PageResponseDto<>(ItemCommentPage);
	}

	// 상품 댓글 수정
	@Transactional
	public ItemCommentResponse updateItemComment(Long itemId, Long itemCommentId, ItemCommentRequest request,
		CustomPrincipal principal) {
		// 수정할 댓글 조회
		ItemComment comment = itemCommentRepository.findById(itemCommentId).
			orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_COMMENT));

		// 유효한 경로인지 확인(해당 itemCommentId의 대상 itemId가 일치하는지 확인)
		if (comment.getItem().getId() != itemId) {
			throw new CustomException(ExceptionCode.INVALID_PATH);
		}

		// 본인이 쓴 댓글인지 확인
		if (comment.getUser().getId() != principal.getId()) {
			throw new CustomException(ExceptionCode.FORBIDDEN_COMMENT_ACCESS);
		}

		// 댓글 수정
		comment.updateItemComment(request.getContent());

		return ItemCommentResponse.from(comment);
	}

	// 상품 댓글 삭제
	@Transactional
	public void deleteItemComment(Long itemId, Long itemCommentId, CustomPrincipal principal) {
		// 삭제할 댓글 조회
		ItemComment comment = itemCommentRepository.findById(itemCommentId).
			orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_COMMENT));

		// 유효한 경로인지 확인(해당 itemCommentId의 대상 itemId가 일치하는지 확인)
		if (comment.getItem().getId() != itemId) {
			throw new CustomException(ExceptionCode.INVALID_PATH);
		}

		// 본인이 쓴 댓글인지 확인
		if (comment.getUser().getId() != principal.getId()) {
			throw new CustomException(ExceptionCode.FORBIDDEN_COMMENT_DELETE);
		}

		// 댓글 삭제
		itemCommentRepository.deleteById(itemCommentId);
	}
}
