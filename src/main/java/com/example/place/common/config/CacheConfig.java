package com.example.place.common.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
@EnableCaching
public class CacheConfig {

	// @Bean
	// public CacheManager cacheManager() {
	// 	CaffeineCacheManager cacheManager = new CaffeineCacheManager("newsfeeds");
	//
	// 	cacheManager.setCaffeine(
	// 		Caffeine.newBuilder()
	// 			.expireAfterWrite(60, TimeUnit.MINUTES)  // 캐시 만료 시간 예: 10분
	// 			.maximumSize(1000)                             // 최대 1000개 엔트리 저장
	// 	);
	//
	// 	return cacheManager;
	// }

	@Bean
	public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
		// 자바 타임 모듈 등록하여 LocalDateTime -> ISO-8601 형태로 직렬화
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		// 타입 정보 포함 (직렬화/역직렬화 시 클래스 정보 유지)
		mapper.activateDefaultTyping(
			LaissezFaireSubTypeValidator.instance,
			ObjectMapper.DefaultTyping.NON_FINAL,
			JsonTypeInfo.As.PROPERTY
		);

		GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(mapper);

		// 공통 기본 설정
		RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
			.serializeKeysWith(
				RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())) // 키는 문자열 직렬화
			.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer)) // 값은 JSON 직렬화
			.disableCachingNullValues(); // null은 캐시에 저장하지 않음

		// 캐시 이름별 TTL 설정
		Map<String, RedisCacheConfiguration> cacheConfigs = new HashMap<>();
		cacheConfigs.put("singleCache", defaultConfig.entryTtl(Duration.ofHours(1)));  // 단건 조회 캐시 - TTL 1시간
		cacheConfigs.put("listCache", defaultConfig.entryTtl(Duration.ofMinutes(10))); // 전체 조회 캐시 - TTL 10분

		return RedisCacheManager.builder(redisConnectionFactory)
			.cacheDefaults(defaultConfig)                  // 기본 설정
			.withInitialCacheConfigurations(cacheConfigs) // 캐시별 커스텀 설정
			.build();
	}
}
