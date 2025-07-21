package com.example.place.domain.itemcomment.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.place.common.dto.ApiResponseDto;
import com.example.place.common.security.jwt.CustomPrincipal;
import com.example.place.domain.itemcomment.dto.request.ItemCommentRequestDto;
import com.example.place.domain.itemcomment.dto.response.ItemCommentResponseDto;
import com.example.place.domain.itemcomment.service.ItemCommentService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/item/{itemId}/comments")
public class ItemCommentController {

	private final ItemCommentService itemCommentService;

	@PostMapping
	public ApiResponseDto saveItemComment(@PathVariable Long itemId, @RequestBody ItemCommentRequestDto request,
		@AuthenticationPrincipal
		CustomPrincipal loginUSer) {

		ItemCommentResponseDto response = itemCommentService.saveItemComment(itemId, request, loginUSer);

		return new ApiResponseDto<>("댓글이 등록되었습니다.", response);
	}

}
