package com.example.place.domain.item.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import com.example.place.domain.Image.entity.Image;
import com.example.place.domain.item.entity.Item;

import lombok.Getter;

@Getter
public class ItemGetAllResponse {
	private Long id;
	private String itemName;
	private Double price;
	private Long count;
	private String mainImageUrl;
	private List<String> tags;

	private ItemGetAllResponse(Long id, String itemName, Double price, Long count, String mainImageUrl,
		List<String> tags) {
		this.id = id;
		this.itemName = itemName;
		this.price = price;
		this.count = count;
		this.mainImageUrl = mainImageUrl;
		this.tags = tags;
	}

	public static ItemGetAllResponse from(Item item, String mainImageUrl) {
		return new ItemGetAllResponse(
			item.getId(),
			item.getItemName(),
			item.getPrice(),
			item.getCount(),
			mainImageUrl,
			item.getItemTags().stream()
				.map(itemTag -> itemTag.getTag().getTagName())
				.collect(Collectors.toList())
		);
	}
}
