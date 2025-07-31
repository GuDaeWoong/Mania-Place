package com.example.place.domain.item.controller;

import com.example.place.common.dto.ApiResponseDto;
import com.example.place.common.dto.PageResponseDto;
import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;
import com.example.place.common.security.jwt.CustomPrincipal;
import com.example.place.domain.item.dto.request.ItemRequest;
import com.example.place.domain.item.dto.response.ItemResponse;
import com.example.place.domain.item.dto.response.ItemSummaryResponse;
import com.example.place.domain.item.service.ItemDeleteService;
import com.example.place.domain.item.service.ItemService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;
    private final ItemDeleteService itemDeleteService;

    /**
     * 상품 생성
     *
     * @param request
     * @param principal
     * @return
     */
    @PostMapping
    public ResponseEntity<ApiResponseDto<ItemResponse>> createItem(
            @Valid @RequestBody ItemRequest request,
            @AuthenticationPrincipal CustomPrincipal principal
    ) {
        ItemResponse item = itemService.createItem(principal.getId(), request);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.of("상품 등록이 완료되었습니다.", item));
    }

    /**
     * 상품 단건 조회
     *
     * @param itemId
     * @return
     */
    @GetMapping("/{itemId}")
    public ResponseEntity<ApiResponseDto<ItemResponse>> getItme(@PathVariable Long itemId) {
        ItemResponse item = itemService.getItem(itemId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.of("상품 조회가 완료되었습니다.", item));
    }

    /**
     * 상품 서치
     *
     * @param keyword
     * @param tags
     * @param userId
     * @param pageable
     * @return
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponseDto<PageResponseDto<ItemSummaryResponse>>> searchItem(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(required = false) Long userId,
            @PageableDefault Pageable pageable
            ) {
        if (tags != null && tags.size() > 10) {
            throw new CustomException(ExceptionCode.TOO_MANY_TAGS);
        }
        PageResponseDto<ItemSummaryResponse> respone = itemService.searchItems(keyword, tags, userId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.of("상품 조회가 완료되었습니다", respone));
    }

    /**
     * 로그인한 유저의 관심태그가 포함된 상품 전체 조회
     *
     * @param pageable
     * @param principal
     * @return
     */
    @GetMapping("/search/interest")
    public ResponseEntity<ApiResponseDto<PageResponseDto<ItemSummaryResponse>>> searchItemWithUserTag(
        @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
        @AuthenticationPrincipal CustomPrincipal principal
    ) {
        PageResponseDto<ItemSummaryResponse> response = itemService.getAllItemsWIthUserTag(principal, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.of("상품 조회가 완료되었습니다", response));
    }

    /**
     * 상품 수정
     *
     * @param itemId
     * @param request
     * @param principal
     * @return
     */
    @PatchMapping("/{itemId}")
    public ResponseEntity<ApiResponseDto<ItemResponse>> updateItem(
            @PathVariable Long itemId,
            @RequestBody ItemRequest request,
            @AuthenticationPrincipal CustomPrincipal principal
    ) {
        ItemResponse updateItem = itemService.updateItem(itemId, request, principal.getId());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.of("상품 수정이 완료되었습니다.", updateItem));
    }

    /**
     * 상품 삭제
     *
     * @param itemId
     * @param principal
     * @return
     */
    @DeleteMapping("/{itemId}")
    public ResponseEntity<ApiResponseDto<Void>> deleteItem(
            @PathVariable Long itemId,
            @AuthenticationPrincipal CustomPrincipal principal
    ) {
        itemDeleteService.removeReferencesAndDeleteItem(itemId, principal.getId());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.of("상품 삭제가 완료되었습니다.", null));
    }

}
