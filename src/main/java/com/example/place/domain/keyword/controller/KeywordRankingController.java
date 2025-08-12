package com.example.place.domain.keyword.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.place.common.dto.ApiResponseDto;
import com.example.place.domain.keyword.service.SearchKeywordService;
import com.example.place.domain.keyword.service.dto.KeywordRankingDto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/keywords")
@RequiredArgsConstructor
@Validated
@Slf4j
public class KeywordRankingController {

	private final SearchKeywordService searchKeywordService;

	/**
	 * 급상승 검색어 조회 (24시간 증가량 기준)
	 * GET /api/keywords/trending?limit=10
	 */
	@GetMapping("/trending")
	public ResponseEntity<ApiResponseDto<List<KeywordRankingDto>>> getTrendingKeywords(
		@RequestParam(defaultValue = "10")
		@Min(value = 1, message = "조회할 개수는 1개 이상이어야 합니다")
		@Max(value = 50, message = "조회할 개수는 50개 이하여야 합니다")
		int limit) {

		List<KeywordRankingDto> trendingKeywords = searchKeywordService.getTopKeywordsLast24Hours(limit);

		return ResponseEntity.status(HttpStatus.OK)
			.body(ApiResponseDto.of("급상승 검색어 조회가 완료되었습니다", trendingKeywords));
	}
}