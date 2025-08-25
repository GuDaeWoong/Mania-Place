package com.example.place.domain.item.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemRequest {

    @NotBlank(message = "상품 이름은 필수입니다")
    private String itemName;
    @NotBlank(message = "상품 설명은 필수입니다.")
    private String itemDescription;
    @NotNull(message = "가격은 필수입니다.")
    @Positive(message = "올바른 가격을 입력해주세요.")
    private Double price;
    @NotNull(message = "수량은 필수입니다.")
    @Min(value = 1, message = "총 수량은 1개 이상이어야 합니다.")
    private Long count;
    private Boolean isLimited;
    private LocalDateTime salesStartAt;
    private LocalDateTime salesEndAt;
    @NotEmpty(message = "최소 1개의 이미지가 있어야합니다.")
    private List<String> imageUrls;
    @NotNull(message = "대표 이미지 인덱스를 입력해주세요.")
    private Integer mainIndex;
    @NotNull(message = "태그 리스트는 null일 수 없습니다.")
    @Size(min = 1, max = 10, message = "태그는 최소 1개 최대 10개가 가능합니다.")
    private Set<String> itemTagNames;

}
