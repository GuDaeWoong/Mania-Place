package com.example.place.domain.newsfeed.service;

import com.example.place.common.dto.PageResponseDto;
import com.example.place.domain.newsfeed.dto.response.NewsfeedListResponse;
import com.example.place.domain.newsfeed.dto.response.NewsfeedResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsfeedCacheService {

	private final RedisTemplate<String, Object> redisTemplate;

	@Value("${cache.newsfeed.ttl:300}")
	private int newsfeedTtl;

	@Value("${cache.newsfeed.list-ttl:60}")
	private int listTtl;

	// 캐시 키 생성 메서드들
	private static final String NEWSFEED_KEY_PREFIX = "newsfeed:";
	private static final String NEWSFEED_LIST_KEY_PREFIX = "newsfeed:list:";
	private static final String NEWSFEED_KEYS_PATTERN = "newsfeed:*";

	private String getNewsfeedKey(Long newsfeedId) {
		return NEWSFEED_KEY_PREFIX + newsfeedId;
	}

	private String getNewsfeedListKey(Pageable pageable) {
		return NEWSFEED_LIST_KEY_PREFIX +
			"page:" + pageable.getPageNumber() +
			":size:" + pageable.getPageSize() +
			":sort:" + pageable.getSort().toString().replaceAll("\\s+", "");
	}

	// 단건 뉴스피드 캐시 조회
	public NewsfeedResponse getNewsfeed(Long newsfeedId) {
		try {
			String key = getNewsfeedKey(newsfeedId);
			Object cached = redisTemplate.opsForValue().get(key);

			if (cached instanceof NewsfeedResponse) {
				log.info("Cache HIT for newsfeed: {}", newsfeedId);
				return (NewsfeedResponse)cached;
			}

			log.info("Cache MISS for newsfeed: {}", newsfeedId);
			return null;
		} catch (Exception e) {
			log.error("Error getting newsfeed from cache: {}", newsfeedId, e);
			return null;
		}
	}

	// 단건 뉴스피드 캐시 저장
	public void putNewsfeed(Long newsfeedId, NewsfeedResponse newsfeedResponse) {
		try {
			String key = getNewsfeedKey(newsfeedId);
			redisTemplate.opsForValue().set(key, newsfeedResponse, Duration.ofSeconds(newsfeedTtl));
			log.info("Cached newsfeed: {}", newsfeedId);
		} catch (Exception e) {
			log.error("Error caching newsfeed: {}", newsfeedId, e);
		}
	}

	// 뉴스피드 목록 캐시 조회
	@SuppressWarnings("unchecked")
	public PageResponseDto<NewsfeedListResponse> getNewsfeedList(Pageable pageable) {
		try {
			String key = getNewsfeedListKey(pageable);
			Object cached = redisTemplate.opsForValue().get(key);

			if (cached instanceof PageResponseDto) {
				log.info("Cache HIT for newsfeed list: page={}, size={}",
					pageable.getPageNumber(), pageable.getPageSize());
				return (PageResponseDto<NewsfeedListResponse>)cached;
			}

			log.info("Cache MISS for newsfeed list: page={}, size={}",
				pageable.getPageNumber(), pageable.getPageSize());
			return null;
		} catch (Exception e) {
			log.error("Error getting newsfeed list from cache", e);
			return null;
		}
	}

	// 뉴스피드 목록 캐시 저장
	public void putNewsfeedList(Pageable pageable, PageResponseDto<NewsfeedListResponse> response) {
		try {
			String key = getNewsfeedListKey(pageable);
			redisTemplate.opsForValue().set(key, response, Duration.ofSeconds(listTtl));
			log.info("Cached newsfeed list: page={}, size={}",
				pageable.getPageNumber(), pageable.getPageSize());
		} catch (Exception e) {
			log.error("Error caching newsfeed list", e);
		}
	}

	// 특정 뉴스피드 캐시 삭제
	public void evictNewsfeed(Long newsfeedId) {
		try {
			String key = getNewsfeedKey(newsfeedId);
			redisTemplate.delete(key);
			log.info("Evicted newsfeed cache: {}", newsfeedId);
		} catch (Exception e) {
			log.error("Error evicting newsfeed cache: {}", newsfeedId, e);
		}
	}

	// 모든 뉴스피드 관련 캐시 삭제 (생성, 수정, 삭제 시)
	public void evictAllNewsfeedCaches() {
		try {
			Set<String> keys = redisTemplate.keys(NEWSFEED_KEYS_PATTERN);
			if (keys != null && !keys.isEmpty()) {
				redisTemplate.delete(keys);
				log.info("Evicted {} newsfeed cache keys", keys.size());
			}
		} catch (Exception e) {
			log.error("Error evicting all newsfeed caches", e);
		}
	}

	// 목록 캐시만 삭제 (새 게시물 생성 시)
	public void evictNewsfeedListCaches() {
		try {
			Set<String> keys = redisTemplate.keys(NEWSFEED_LIST_KEY_PREFIX + "*");
			if (keys != null && !keys.isEmpty()) {
				redisTemplate.delete(keys);
				log.info("Evicted {} newsfeed list cache keys", keys.size());
			}
		} catch (Exception e) {
			log.error("Error evicting newsfeed list caches", e);
		}
	}
}