package com.example.place.common.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
public class HealthSummaryController {

	// @Autowired
	// private DatabaseHealthIndicator databaseHealth;

	// @Autowired
	// private RedisHealthIndicator redisHealth;

	@Autowired
	private ServerHealthIndicator serverHealth;

	@GetMapping("/simple")
	public ResponseEntity<String> getSimpleStatus() {
		try {
			// Health dbStatus = databaseHealth.health();
			// Health redisStatus = redisHealth.health();
			Health serverStatus = serverHealth.health();

			boolean allHealthy =
				// "UP".equals(dbStatus.getStatus().getCode()) &&
				// "UP".equals(redisStatus.getStatus().getCode()) &&
				"UP".equals(serverStatus.getStatus().getCode());

			if (allHealthy) {
				// return ResponseEntity.ok("Mania-Place is healthy - DB:UP, Redis:UP, Server:UP");
				return ResponseEntity.ok("Mania-Place is healthy Server:UP");
			} else {
				// String message = String.format("Mania-Place has issues - DB:%s, Redis:%s, Server:%s",
				String message = String.format("Mania-Place has issues - Server:%s",
					// dbStatus.getStatus().getCode(),
					// redisStatus.getStatus().getCode(),
					serverStatus.getStatus().getCode());

				return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(message);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("Health check failed: " + e.getMessage());
		}
	}
}
