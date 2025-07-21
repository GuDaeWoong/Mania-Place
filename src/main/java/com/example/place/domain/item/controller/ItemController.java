package com.example.place.domain.item.controller;

import com.example.place.common.dto.ApiResponseDto;
import com.example.place.domain.item.dto.request.CreateItemRequest;
import com.example.place.domain.item.service.ItemService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;

    /**
     * 수정필요
     * @param dto
     * @return
     */
    @PostMapping
    public ApiResponseDto<String> createItem(@RequestBody CreateItemRequest dto) {
        itemService.createItem(dto);
        return new ApiResponseDto<>("상품 등록이 완료되었습니다.", "수정필요");
    }



}
