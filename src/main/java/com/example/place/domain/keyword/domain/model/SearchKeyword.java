package com.example.place.domain.keyword.domain.model;

import com.example.place.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "search_keyword")
public class SearchKeyword extends BaseEntity {
	@Id
	@Column(length = 100)
	private String keyword;

	@Column(nullable = false)
	private Long count = 0L;

	private SearchKeyword(String keyword) {
		this.keyword = keyword;
		this.count = 1L;
	}

	public static SearchKeyword of(String keyword) {
		return new SearchKeyword(keyword);
	}

	public void incrementCount() {
		this.count++;
	}
}