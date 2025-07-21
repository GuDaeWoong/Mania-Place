package com.example.place.domain.item.controller;

import com.example.place.common.dto.ApiResponseDto;
import com.example.place.domain.item.dto.request.CreateItemRequest;
import com.example.place.domain.item.dto.response.CreateItemResponse;
import com.example.place.domain.item.service.ItemService;
import com.example.place.domain.user.entity.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;

    /**
     * 상품 생성 기존 Tag가 있다면 사용 없다면 생성해서 상품등록
     * @param request
     * @param user
     * @return
     */
    @PostMapping
    public ApiResponseDto<CreateItemResponse> createItem(
            @RequestBody CreateItemRequest request,
            @AuthenticationPrincipal User user
    ) {
        CreateItemResponse item = itemService.createItem(user.getId(), request);
        return new ApiResponseDto<>("상품 등록이 완료되었습니다.", item);
    }

    /**
     * 상품 단건 조회
     * @param itemId
     * @return
     */
    @GetMapping("/{itemId}")
    public ApiResponseDto<CreateItemResponse> readItem(@PathVariable Long itemId) {
        return new ApiResponseDto<>("상품 조회가 완료되었습니다.", itemService.readItem(itemId));
    }

    /**
     * 상품 단건 수정
     * @param request
     * @return
     */
    @PatchMapping("/{itemId}")
    public ApiResponseDto<CreateItemResponse> updateItem(
            @PathVariable Long itemId,
            @RequestBody CreateItemRequest request
    ) {
        CreateItemResponse updateItem = itemService.updateItem(itemId, request);
        return new ApiResponseDto<>("상품 수정이 완료되었습니다.", updateItem);
    }

    @DeleteMapping("/{itemId}")
    public ApiResponseDto<String> deleteItem(
            @PathVariable Long itemId,
            @AuthenticationPrincipal User user
    ) {
        itemService.deleteItem(itemId, user.getId());
        return new ApiResponseDto<>("상품 삭제가 완료되었습니다.", null);
    }




}
