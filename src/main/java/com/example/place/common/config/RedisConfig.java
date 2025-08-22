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

	// 환경변수를 한 번만 읽어서 재사용
	private final String host = System.getenv("REDIS_HOST");
	private final String port = System.getenv("REDIS_PORT");
	private final String username = System.getenv("REDIS_USERNAME");
	private final String password = System.getenv("REDIS_PASSWORD");

	@Bean
	public LettuceConnectionFactory redisConnectionFactory() {
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
	public RedissonClient redissonClient() {
		Config config = new Config();
		config.useSingleServer()
			.setAddress("rediss://" + host + ":" + port)
			.setUsername(username)
			.setPassword(password)
			.setConnectionMinimumIdleSize(5)
			.setConnectionPoolSize(20)
			.setIdleConnectionTimeout(10000)
			.setConnectTimeout(10000)
			.setTimeout(3000)
			.setRetryAttempts(3)
			.setRetryInterval(1500);

		return Redisson.create(config);
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
