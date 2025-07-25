package com.example.place.domain.tag.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TagRequest {
    @NotBlank(message = "태그 이름은 공백일 수 없습니다.")
    @Size(max=70, message = "태그이름은 70자를 넘을 수 없습니다.")
    private String tagName;
}
