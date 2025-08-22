package com.example.place.common.config;

import java.time.Duration;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

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

	@Bean
	public LettuceConnectionFactory redisConnectionFactory() {
		// 환경변수에서 동일한 정보 가져오기
		String host = System.getenv("REDIS_HOST");
		String port = System.getenv("REDIS_PORT");
		String username = System.getenv("REDIS_USERNAME");
		String password = System.getenv("REDIS_PASSWORD");

		// SSL/TLS 설정
		RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
		redisConfig.setHostName(host);
		redisConfig.setPort(Integer.parseInt(port));
		redisConfig.setUsername(username);
		redisConfig.setPassword(password);

		// Lettuce SSL 설정
		LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
			.useSsl()
			.and()
			.commandTimeout(Duration.ofSeconds(3))
			.build();

		return new LettuceConnectionFactory(redisConfig, clientConfig);
	}

	@Bean
	public RedisTemplate<String, String> redisTemplate(LettuceConnectionFactory connectionFactory) {
		RedisTemplate<String, String> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);

		// String 직렬화 설정
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setHashValueSerializer(new StringRedisSerializer());

		template.afterPropertiesSet();
		return template;
	}
}


// import org.redisson.Redisson;
// import org.redisson.api.RedissonClient;
// import org.redisson.config.Config;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
//
// @Configuration
// public class RedisConfig {
//
// 	@Value("${REDIS_HOST}")
// 	private String redisHost;
//
// 	@Value("${REDIS_PORT}")
// 	private int redisPort;
//
// 	@Bean
// 	public RedissonClient redissonClient() {
// 		Config config = new Config();
// 		config.useSingleServer()
// 			// .setAddress("redis://localhost:6379")
// 			.setAddress("redis://" + redisHost + ":" + redisPort)
// 			// 최소 연결 개수
// 			.setConnectionMinimumIdleSize(5)
// 			// 최대 연결 개수
// 			.setConnectionPoolSize(20)
// 			// 연결 유지시간 10초 = 10000ms
// 			.setIdleConnectionTimeout(10000)
// 			// Redis 서버에 연결을 시도할 때의 최대 대기 시간 10초
// 			.setConnectTimeout(10000)
// 			// Redis 서버로부터 응답을 기다리는 최대 시간 3초
// 			.setTimeout(3000)
// 			// 연결 재시도 횟수 3번
// 			.setRetryAttempts(3)
// 			// 연결 재시도 간의 대기시간 1.5초
// 			.setRetryInterval(1500);
//
// 		return Redisson.create(config);
// 	}
// }
