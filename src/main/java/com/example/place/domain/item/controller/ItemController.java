package com.example.place.domain.item.controller;

import com.example.place.common.dto.ApiResponseDto;
import com.example.place.common.dto.PageResponseDto;
import com.example.place.common.security.jwt.CustomPrincipal;
import com.example.place.domain.item.dto.request.ItemRequest;
import com.example.place.domain.item.dto.response.ItemResponse;
import com.example.place.domain.item.service.ItemDeleteService;
import com.example.place.domain.item.service.ItemService;
import org.springframework.data.domain.Pageable;
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
     * 상품 생성 기존 Tag가 있다면 사용 없다면 생성해서 상품등록
     *
     * @param request
     * @param
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
     * 원하는 값으로 조회
     *
     * @param keyword
     * @param tags
     * @param userId
     * @param pageable
     * @return
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponseDto<PageResponseDto<ItemResponse>>> searchItem(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(required = false) Long userId,
            @PageableDefault Pageable pageable
            ) {
        PageResponseDto<ItemResponse> result = itemService.searchItems(keyword, tags, userId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.of("상품 조회가 완료되었습니다", result));
    }

    /**
     * 상품 단건 수정
     *
     * @param request
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
