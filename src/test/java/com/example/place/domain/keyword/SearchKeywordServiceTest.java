package com.example.place.domain.keyword;

import com.example.place.domain.keyword.service.SearchKeywordService;
import com.example.place.domain.keyword.service.dto.KeywordRankingDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.time.Duration;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class SearchKeywordServiceTest {

	private static final String TEST_KEYWORD = "SpringBoot";
	private static final String NORMALIZED_KEYWORD = "springboot";
	private static final long TTL_HOURS = 25L;

	@Mock
	private RedisTemplate<String, String> redisTemplate;

	@Mock
	private ZSetOperations<String, String> zSetOperations;

	@InjectMocks
	private SearchKeywordService searchKeywordService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);
	}

	/**
	 * addKeyword(null/빈 문자열) → Redis 호출이 일어나지 않아야 한다.
	 */
	@Test
	void addKeyword_nullOrEmpty_shouldIgnore() {
		searchKeywordService.addKeyword(null);
		searchKeywordService.addKeyword("   ");

		verify(redisTemplate, never()).opsForZSet();
	}

	/**
	 * addKeyword(정상 키워드) → incrementScore 실행 + TTL 설정 확인
	 */
	@Test
	void addKeyword_validKeyword_shouldIncrementScoreAndSetExpire() {
		when(redisTemplate.getExpire(anyString())).thenReturn(-1L); // TTL 없음 가정

		searchKeywordService.addKeyword(TEST_KEYWORD);

		verify(zSetOperations).incrementScore(anyString(), eq(NORMALIZED_KEYWORD), eq(1.0));
		verify(redisTemplate).expire(anyString(), eq(Duration.ofHours(TTL_HOURS)));
	}

	/**
	 * addKeyword(정상 키워드 + TTL 존재) → TTL 재설정하지 않아야 한다.
	 */
	@Test
	void addKeyword_whenTTLExists_shouldNotResetExpire() {
		when(redisTemplate.getExpire(anyString())).thenReturn(1000L); // TTL 이미 있음

		searchKeywordService.addKeyword(TEST_KEYWORD);

		verify(zSetOperations).incrementScore(anyString(), eq(NORMALIZED_KEYWORD), eq(1.0));
		verify(redisTemplate, never()).expire(anyString(), any());
	}
	/**
	 * getTopKeywordsLast24Hours(결과 없음)
	 * → 빈 리스트 반환
	 */
	@Test
	void getTopKeywordsLast24Hours_emptyResult_shouldReturnEmptyList() {
		when(zSetOperations.reverseRangeWithScores(anyString(), anyLong(), anyLong()))
			.thenReturn(Collections.emptySet());

		List<KeywordRankingDto> result = searchKeywordService.getTopKeywordsLast24Hours(3);

		assertTrue(result.isEmpty());
	}

	/**
	 * getTopKeywordsLast24Hours(예외 발생)
	 * → 빈 리스트 반환
	 */
	@Test
	void getTopKeywordsLast24Hours_exception_shouldReturnEmptyList() {
		when(zSetOperations.reverseRangeWithScores(anyString(), anyLong(), anyLong()))
			.thenThrow(new RuntimeException("Redis error"));

		List<KeywordRankingDto> result = searchKeywordService.getTopKeywordsLast24Hours(3);

		assertTrue(result.isEmpty());
	}
}