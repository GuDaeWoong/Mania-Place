package com.example.place.domain.itemcomment.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.place.common.dto.ApiResponseDto;
import com.example.place.common.dto.PageResponseDto;
import com.example.place.common.security.jwt.CustomPrincipal;
import com.example.place.domain.itemcomment.dto.request.ItemCommentRequest;
import com.example.place.domain.itemcomment.dto.response.ItemCommentResponse;
import com.example.place.domain.itemcomment.service.ItemCommentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/item/{itemId}/comments")
public class ItemCommentController {

	private final ItemCommentService itemCommentService;

	/**
	 * 상품 댓글 등록
	 *
	 * @param itemId
	 * @param request
	 * @param principal
	 * @return 등록된 상품 댓글
	 */
	@PostMapping
	public ResponseEntity<ApiResponseDto<ItemCommentResponse>> saveItemComment(
		@PathVariable Long itemId,
		@Valid @RequestBody ItemCommentRequest request,
		@AuthenticationPrincipal CustomPrincipal principal) {

		ItemCommentResponse response = itemCommentService.saveItemComment(itemId, request, principal);

		return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.of("댓글이 등록되었습니다.", response));
	}

	/**
	 * 상품별 댓글 조회
	 *
	 * @param itemId
	 * @param pageable
	 * @return 조회된 상품 댓글들
	 */
	@GetMapping
	public ResponseEntity<ApiResponseDto<PageResponseDto<ItemCommentResponse>>> readItemComment(
		@PathVariable Long itemId,
		@PageableDefault Pageable pageable) {

		PageResponseDto<ItemCommentResponse> response = itemCommentService.readItemComment(itemId, pageable);

		return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.of("댓글이 조회되었습니다.", response));
	}

	/**
	 * 댓글 수정
	 *
	 * @param itemCommentId
	 * @param request
	 * @param principal
	 * @return
	 */
	@PatchMapping("/{itemCommentId}")
	public ResponseEntity<ApiResponseDto<ItemCommentResponse>> updateItemComment(
		@PathVariable Long itemId,
		@PathVariable Long itemCommentId,
		@Valid @RequestBody ItemCommentRequest request,
		@AuthenticationPrincipal CustomPrincipal principal) {

		ItemCommentResponse response = itemCommentService.updateItemComment(itemId, itemCommentId, request, principal);

		return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.of("댓글을 수정하였습니다.", response));
	}

	/**
	 * 상품 댓글 삭제
	 *
	 * @param itemId
	 * @param itemCommentId
	 * @param principal
	 * @return
	 */
	@DeleteMapping("/{itemCommentId}")
	public ResponseEntity<ApiResponseDto> deleteItemComment(
		@PathVariable Long itemId,
		@PathVariable Long itemCommentId,
		@AuthenticationPrincipal CustomPrincipal principal) {

		itemCommentService.deleteItemComment(itemId, itemCommentId, principal);

		return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.of("댓글을 삭제하였습니다.", null));
	}
}
