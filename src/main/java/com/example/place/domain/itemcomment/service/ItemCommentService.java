package com.example.place.domain.itemcomment.service;

import org.springframework.stereotype.Service;

import com.example.place.common.security.jwt.CustomPrincipal;
import com.example.place.domain.item.entity.Item;
import com.example.place.domain.item.service.ItemService;
import com.example.place.domain.itemcomment.dto.request.ItemCommentRequestDto;
import com.example.place.domain.itemcomment.dto.response.ItemCommentResponseDto;
import com.example.place.domain.itemcomment.entity.ItemComment;
import com.example.place.domain.itemcomment.repository.ItemCommentRepository;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.user.service.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemCommentService {

	private final ItemCommentRepository itemCommentRepository;
	private final ItemService itemService;
	private final UserService userService;

	@Transactional
	public ItemCommentResponseDto saveItemComment(Long itemId, ItemCommentRequestDto request,
		CustomPrincipal loginUser) {
		// 로그인 유저 조회
		User user = userService.findUserById(loginUser.getId());

		// 대상 상품 조회
		Item item = itemService.findItemById(itemId);

		// 댓글 저장
		ItemComment itemComment = ItemComment.of(user, item, request.getContent());
		ItemComment saveItemComment = itemCommentRepository.save(itemComment);

		// 응답 DTO로 반환
		return ItemCommentResponseDto.of(saveItemComment);
	}
}
