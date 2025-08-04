package com.example.place.domain.item.dto;

import com.example.place.common.dto.PageResponseDto;
import com.example.place.domain.item.dto.response.ItemGetAllResponse;

import lombok.Getter;

@Getter
public class ItemsAndIsFindByUserTag {
	private PageResponseDto<ItemGetAllResponse> response;
	private boolean isFindByUserTag;

	private ItemsAndIsFindByUserTag(PageResponseDto<ItemGetAllResponse> response, boolean isFindByUserTag) {
		this.response = response;
		this.isFindByUserTag = isFindByUserTag;
	}

	public static ItemsAndIsFindByUserTag of(PageResponseDto<ItemGetAllResponse> response, boolean isFindByUserTag) {
		return new ItemsAndIsFindByUserTag(response, isFindByUserTag);
	}
}
