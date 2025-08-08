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
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

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
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);

		// ObjectMapper 설정 (LocalDateTime 등을 위한 JavaTimeModule 추가)
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());

		// key를 string 직렬화
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());

		// value를 json 직렬화
		GenericJackson2JsonRedisSerializer jsonSerializer =
			new GenericJackson2JsonRedisSerializer(objectMapper);
		template.setValueSerializer(jsonSerializer);
		template.setHashValueSerializer(jsonSerializer);

		template.afterPropertiesSet();
		return template;
	}

}
