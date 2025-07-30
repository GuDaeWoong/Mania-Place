package com.example.place.domain.newsfeed.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NewsfeedRequest {

	@NotBlank(message = "제목을 입력해주세요.")
	private String title;
	@NotBlank(message = "내용을 입력해주세요.")
	private String content;
	@NotEmpty(message = "최소 1개의 이미지가 있어야합니다.")
	private List<String> imageUrls;
	@NotNull(message = "대표 이미지 인덱스를 입력해주세요.")
	private Integer mainIndex;
}
