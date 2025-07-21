package com.example.place.domain.item.dto.response;

import com.example.place.domain.item.entity.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class ItemResponse {
    private String itemName;
    private String itemDescription;
    private Double price;
    private Long count;
    private LocalDateTime salesStartAt;
    private LocalDateTime salesEndAt;
    private List<String> tags;

    public static ItemResponse from(Item item) {
        List<String> tagNames = item.getItemTags().stream()
                .map(itemTag -> itemTag.getTag().getTagName())
                .toList();

        ItemResponse response = new ItemResponse();
        response.itemName = item.getItemName();
        response.itemDescription = item.getItemDescription();
        response.price = item.getPrice();
        response.count = item.getCount();
        response.salesStartAt = item.getSalesStartAt();
        response.salesEndAt = item.getSalesEndAt();
        response.tags = tagNames;
        return response;
    }
}
