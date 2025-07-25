package com.example.place.domain.tag.util;

public class TagUtil {

	// 태그 정규화(공백 제거, 특수문자 제거)
	public static String normalizeTag(String tag) {
		return tag.toLowerCase()
			.replaceAll("[^a-zA-Z0-9가-힣]", "");
	}
}
