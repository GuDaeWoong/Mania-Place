package com.example.place.domain.keyword.service.dto;

import lombok.Getter;

@Getter
public class KeywordRankingDto {

	private String keyword;
	private Long count;

	public KeywordRankingDto(String keyword, Long count) {
		this.keyword = keyword;
		this.count = count;
	}
}