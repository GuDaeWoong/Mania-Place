package com.example.place.domain.item.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateItemResponse {
    private String itemName;
    private String itemDescription;
    private Double price;
    private Long count;
    private LocalDateTime salesStartAt;
    private LocalDateTime salesEndAt;
    private List<String> tags;
}
