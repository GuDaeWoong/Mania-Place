package com.example.place.domain.item.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import com.example.place.domain.item.entity.Item;

import lombok.Getter;

@Getter
public class ItemSummaryResponse {
	private Long id;
	private String itemName;
	private Double price;
	private Long count;
	private List<String> tags;

	private ItemSummaryResponse(Long id, String itemName, Double price, Long count, List<String> tags) {
		this.id = id;
		this.itemName = itemName;
		this.price = price;
		this.count = count;
		this.tags = tags;
	}

	public static ItemSummaryResponse from(Item item) {
		return new ItemSummaryResponse(
			item.getId(),
			item.getItemName(),
			item.getPrice(),
			item.getCount(),
			item.getItemTags().stream()
				.map(itemTag -> itemTag.getTag().getTagName())
				.collect(Collectors.toList())
		);
	}
}
