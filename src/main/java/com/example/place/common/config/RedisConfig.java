package com.example.place.common.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.example.place.common.dto.PageResponseDto;
import com.example.place.domain.newsfeed.dto.response.NewsfeedListResponse;
import com.example.place.domain.newsfeed.dto.response.NewsfeedResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class RedisConfig {
	@Bean
	public RedissonClient redissonClient() {
		Config config = new Config();
		config.useSingleServer()
			.setAddress("redis://localhost:6379")
			// 최소 연결 개수
			.setConnectionMinimumIdleSize(5)
			// 최대 연결 개수
			.setConnectionPoolSize(20)
			// 연결 유지시간 10초 = 10000ms
			.setIdleConnectionTimeout(10000)
			// Redis 서버에 연결을 시도할 때의 최대 대기 시간 10초
			.setConnectTimeout(10000)
			// Redis 서버로부터 응답을 기다리는 최대 시간 3초
			.setTimeout(3000)
			// 연결 재시도 횟수 3번
			.setRetryAttempts(3)
			// 연결 재시도 간의 대기시간 1.5초
			.setRetryInterval(1500);

		return Redisson.create(config);
	}

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		return new LettuceConnectionFactory("localhost", 6379);
	}

	// redis 템플릿

	@Bean
	public RedisTemplate<String, NewsfeedResponse> newsfeedRedisTemplate(
		RedisConnectionFactory cf, ObjectMapper objectMapper) {

		RedisTemplate<String, NewsfeedResponse> t = new RedisTemplate<>();
		t.setConnectionFactory(cf);
		t.setKeySerializer(new StringRedisSerializer());

		// 단건은 구체 타입 직렬화로
		Jackson2JsonRedisSerializer<NewsfeedResponse> vs =
			new Jackson2JsonRedisSerializer<>(objectMapper, NewsfeedResponse.class);
		t.setValueSerializer(vs);
		t.afterPropertiesSet();
		return t;
	}

	@Bean
	public RedisTemplate<String, PageResponseDto<NewsfeedListResponse>> newsfeedListRedisTemplate(
		RedisConnectionFactory cf, ObjectMapper objectMapper) {

		RedisTemplate<String, PageResponseDto<NewsfeedListResponse>> t = new RedisTemplate<>();
		t.setConnectionFactory(cf);
		t.setKeySerializer(new StringRedisSerializer());

		// 전체는 페이지 DTO를 받으니까 generic 직렬화로 타입정보 포함해야 안전하다
		GenericJackson2JsonRedisSerializer vs = new GenericJackson2JsonRedisSerializer(objectMapper);
		t.setValueSerializer(vs);

		t.afterPropertiesSet();
		return t;
	}
}
