package com.example.place.common.health;

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



