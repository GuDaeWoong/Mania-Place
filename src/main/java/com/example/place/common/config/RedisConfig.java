package com.example.place.common.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class RedisConfig {
	@Bean
	public RedissonClient redissonClient() {
		// 환경변수에서 Serverless Redis 정보 가져오기
		String host = System.getenv("REDIS_HOST");
		String port = System.getenv("REDIS_PORT");
		String username = System.getenv("REDIS_USERNAME"); // Redis 6.x 이상
		String password = System.getenv("REDIS_PASSWORD");
		Config config = new Config();
		config.useSingleServer()
			.setAddress("rediss://" + host + ":" + port) // TLS 연결
			.setUsername(username)
			.setPassword(password)
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
}
