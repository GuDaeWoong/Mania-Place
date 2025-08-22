// package com.example.place.common.health;
//
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.actuate.health.Health;
// import org.springframework.boot.actuate.health.HealthIndicator;
// import org.springframework.data.redis.connection.RedisConnection;
// import org.springframework.data.redis.connection.RedisConnectionFactory;
// import org.springframework.stereotype.Component;
//
// @Component
// public class RedisHealthIndicator implements HealthIndicator {
//
// 	@Autowired(required = false)
// 	private RedisConnectionFactory redisConnectionFactory;
//
// 	@Override
// 	public Health health() {
// 		if (redisConnectionFactory == null) {
// 			return Health.unknown().build();
// 		}
//
// 		RedisConnection connection = null;
// 		try {
// 			// Redis 서버에 연결
// 			connection = redisConnectionFactory.getConnection();
// 			// Redis에 PING 명령을 보냄
// 			String pong = connection.ping();
//
// 			if ("PONG".equals(pong)) {
// 				return Health.up().build();
// 			} else {
// 				return Health.down().build();
// 			}
// 		} catch (Exception e) {
// 			return Health.down().build();
// 		} finally {
// 			if (connection != null) {
// 				try {
// 					connection.close();
// 				} catch (Exception e) {
// 				}
// 			}
// 		}
// 	}
// }
package com.example.place.common.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

@Component
public class RedisHealthIndicator implements HealthIndicator {

	@Autowired(required = false)
	private RedisConnectionFactory redisConnectionFactory;

	// Serverless Redis를 위한 긴 타임아웃 설정
	private static final int TIMEOUT_MS = 10000; // 10초

	@Override
	public Health health() {
		if (redisConnectionFactory == null) {
			return Health.unknown()
				.withDetail("reason", "RedisConnectionFactory not found")
				.build();
		}

		RedisConnection connection = null;
		try {
			// Redis 서버에 연결 (Serverless는 초기 연결이 느릴 수 있음)
			connection = redisConnectionFactory.getConnection();

			// Serverless Redis를 위한 긴 타임아웃으로 PING
			long startTime = System.currentTimeMillis();
			String pong = connection.ping();
			long responseTime = System.currentTimeMillis() - startTime;

			if ("PONG".equals(pong)) {
				return Health.up()
					.withDetail("responseTime", responseTime + "ms")
					.withDetail("type", "serverless")
					.build();
			} else {
				return Health.down()
					.withDetail("reason", "Unexpected ping response: " + pong)
					.build();
			}

		} catch (Exception e) {
			return Health.down()
				.withDetail("error", e.getMessage())
				.withDetail("type", "serverless")
				.withDetail("suggestion", "Redis Serverless may be in cold start - try again")
				.build();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (Exception e) {
				}
			}
		}
	}
}