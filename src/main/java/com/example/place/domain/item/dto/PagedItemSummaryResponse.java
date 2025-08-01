package com.example.place.domain.item.dto;

import com.example.place.common.dto.PageResponseDto;
import com.example.place.domain.item.dto.response.ItemSummaryResponse;

import lombok.Getter;

@Getter
public class PagedItemSummaryResponse {
	private PageResponseDto<ItemSummaryResponse> response;
	private boolean isFindByUserTag;

	private PagedItemSummaryResponse(PageResponseDto<ItemSummaryResponse> response, boolean isFindByUserTag) {
		this.response = response;
		this.isFindByUserTag = isFindByUserTag;
	}

	public static PagedItemSummaryResponse of(PageResponseDto<ItemSummaryResponse> response, boolean isFindByUserTag) {
		return new PagedItemSummaryResponse(response, isFindByUserTag);
	}
}
