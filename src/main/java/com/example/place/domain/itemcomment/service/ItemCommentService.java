package com.example.place.domain.itemcomment.service;

import org.springframework.stereotype.Service;

import com.example.place.common.security.jwt.CustomPrincipal;
import com.example.place.domain.item.entity.Item;
import com.example.place.domain.item.service.ItemService;
import com.example.place.domain.itemcomment.dto.request.ItemCommentRequest;
import com.example.place.domain.itemcomment.dto.response.ItemCommentResponse;
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
	public ItemCommentResponse saveItemComment(Long itemId, ItemCommentRequest request,
		CustomPrincipal principal) {
		// 로그인 유저 조회
		User user = userService.findUserById(principal.getId());

		// 대상 상품 조회
		Item item = itemService.findByIdOrElseThrow(itemId);

		// 댓글 저장
		ItemComment itemComment = ItemComment.of(user, item, request.getContent());
		ItemComment saveItemComment = itemCommentRepository.save(itemComment);

		// 응답 DTO로 반환
		return ItemCommentResponse.of(saveItemComment);
	}
}
