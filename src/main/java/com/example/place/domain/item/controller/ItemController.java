package com.example.place.domain.item.controller;

import com.example.place.common.dto.ApiResponseDto;
import com.example.place.common.security.jwt.CustomPrincipal;
import com.example.place.domain.item.dto.request.ItemRequest;
import com.example.place.domain.item.dto.response.ItemResponse;
import com.example.place.domain.item.service.ItemDeleteService;
import com.example.place.domain.item.service.ItemService;
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
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDto.of("상품 등록이 완료되었습니다.", item)) ;
    }

    /**
     * 상품 단건 조회
     * @param itemId
     * @return
     */
    @GetMapping("/{itemId}")
    public ResponseEntity<ApiResponseDto<ItemResponse>> readItem(@PathVariable Long itemId) {
        ItemResponse item = itemService.readItem(itemId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.of("상품 조회가 완료되었습니다.", item));
    }

    /**
     * 원하는 값으로 조회
     * @param keyword
     * @param tags
     * @param userId
     * @return
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponseDto<List<ItemResponse>>> searchItem(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(required = false) Long userId
    ) {
        List<ItemResponse> result = itemService.searchItem(keyword, tags, userId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.of("상품 조회가 완료되었습니다", result));
    }

    /**
     * 상품 단건 수정
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
     * @param itemId
     * @param principal
     * @return
     */
    @DeleteMapping("/{itemId}")
    public ResponseEntity<ApiResponseDto<Void>> deleteItem(
            @PathVariable Long itemId,
            @AuthenticationPrincipal CustomPrincipal principal
    ) {
        itemDeleteService.deleteItemAndclearItemPK(itemId, principal.getId());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.of("상품 삭제가 완료되었습니다.", null));
    }

}
