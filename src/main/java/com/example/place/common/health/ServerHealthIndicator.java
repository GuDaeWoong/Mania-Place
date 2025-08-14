package com.example.place.common.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class ServerHealthIndicator implements HealthIndicator {

	@Override
	public Health health() {
		try {
			// 간단한 계산으로 서버 확인
			double result = 0;
			for (int i = 0; i < 1000; i++) {
				result += Math.sqrt(i);
			}

			return Health.up().build();

		} catch (Exception e) {
			return Health.down().build();
		}
	}
}
