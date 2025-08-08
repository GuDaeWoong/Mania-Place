package com.example.place.common.health;

import java.sql.Connection;
import javax.sql.DataSource;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class DatabaseHealthIndicator implements HealthIndicator {

	private final DataSource dataSource;

	public DatabaseHealthIndicator(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public Health health() {
		// DB에 실제 연결
		try (Connection connection = dataSource.getConnection()) {
			// DB 연결이 되었는지 확인
			boolean isValid = connection.isValid(10);

			if (isValid) {
				return Health.up().build();
			} else {
				return Health.down().build();
			}
		} catch (Exception e) {
			return Health.down().build();
		}
	}
}
