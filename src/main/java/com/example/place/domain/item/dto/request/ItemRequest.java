package com.example.place.domain.item.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class ItemRequest {
    @NotNull(message = "사용자 ID는 필수입니다.")
    private Long userId;
    @NotBlank(message = "사용자 이름은 필수입니다")
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
    private List<String> images;
    @NotNull(message = "대표 이미지 번호를 입력해주세요")
    private int mainIndex;
    private List<String> itemTagNames;


}
