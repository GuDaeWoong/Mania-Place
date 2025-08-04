package com.example.place.domain.item.dto.response;

import com.example.place.domain.Image.dto.ImageDto;
import com.example.place.domain.Image.entity.Image;
import com.example.place.domain.item.entity.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ItemResponse {
    private Long id;
    private String itemName;
    private String itemDescription;
    private Double price;
    private Long count;
    private Boolean isLimited;
    private LocalDateTime salesStartAt;
    private LocalDateTime salesEndAt;
    private List<String> imageUrls;
    private int mainIndex;
    private List<String> tags;

    public static ItemResponse from(Item item, ImageDto imageDto) {
        List<String> tagNames = item.getItemTags().stream()
                .map(itemTag -> itemTag.getTag().getTagName())
                .toList();

        ItemResponse response = new ItemResponse();
        response.id = item.getId();
        response.itemName = item.getItemName();
        response.itemDescription = item.getItemDescription();
        response.price = item.getPrice();
        response.count = item.getCount();
        response.isLimited = item.getIsLimited();

        response.salesStartAt = item.getSalesStartAt();
        response.salesEndAt = item.getSalesEndAt();
        response.imageUrls = imageDto.getImageUrls();
        response.mainIndex = imageDto.getMainIndex();
        response.tags = tagNames;
        return response;
    }
}
