package com.example.place.common.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.Getter;

@Getter
public class PageResponseDto<T> {

	/**
	 * content: 페이지에 포함된 데이터 List
	 * page: 현재 페이지 번호
	 * totalPages: 전체 페이지 수
	 */

	private final List<T> content;
	private final int page;
	private final int totalPages;

	public PageResponseDto(Page<T> page) {
		this.content = page.getContent();
		this.page = page.getNumber();
		this.totalPages = page.getTotalPages();
	}
}
