package com.example.place.domain.user.dto;

import java.util.LinkedHashSet;
import java.util.Set;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserUpdateRequest {
	@Size(min = 2, max = 30, message = "2자 이상 30자 이하로 입력해주세요.")
	private final String name;
	@Size(min = 2, max = 30, message = "2자 이상 30자 이하로 입력해주세요.")
	private final String nickname;
	private final String imageUrl;
	@Size(max = 5, message = "태그는 최대 5개까지 등록할 수 있습니다.")
	private final Set<@Size(max = 70, message = "태그명은 70자를 초과할 수 없습니다.")String> tags = new LinkedHashSet<>();

	public UserUpdateRequest(String name, String nickname, String imageUrl) {
		this.name = name;
		this.nickname = nickname;
		this.imageUrl = imageUrl;
	}
}
