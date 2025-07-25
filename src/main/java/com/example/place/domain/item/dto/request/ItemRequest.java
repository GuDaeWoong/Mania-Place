package com.example.place.domain.item.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class ItemRequest {

    @NotBlank(message = "상품 이름은 필수입니다")
    private String itemName;
    @NotBlank(message = "상품 설명은 필수입니다.")
    private String itemDescription;
    @NotNull(message = "가격은 필수입니다.")
    @Positive(message = "올바른 가격을 입력해주세요.")
    private Double price;
    @NotNull(message = "수량은 필수입니다.")
    private Long count;
    private LocalDateTime salesStartAt;
    private LocalDateTime salesEndAt;
    @NotEmpty(message = "최소 1개의 이미지가 있어야합니다.")
    private List<String> imageUrls;
    private Integer mainIndex;
    @NotNull(message = "태그 리스트는 null일 수 없습니다.")
    @Size(min = 1, message = "최소 하나 이상의 태그가 필요합니다.")
    private List<String> itemTagNames;


}
