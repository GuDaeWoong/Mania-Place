package com.example.place.domain.tag.repository;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.dao.CannotAcquireLockException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TagJdbcRepository {

	private final JdbcTemplate jdbcTemplate;

	public void batchInsertIgnoreTags(Set<String> newTagNames) {
		if (newTagNames.isEmpty()) return;

		// ('tag1'), ('tag2') 형태로 SQL 문자열 생성
		String values = newTagNames.stream()
			.sorted()
			.map(tag -> String.format("('%s')", tag.replace("'", "''")))
			.collect(Collectors.joining(", "));

		String sql = "INSERT IGNORE INTO tags (tag_name) VALUES " + values;

		try {
			jdbcTemplate.update(sql);
		} catch (CannotAcquireLockException e) {
			handleDeadlock(newTagNames, 0);
		}
	}

	private void handleDeadlock(Set<String> tagNames, int retryCount) {
		if (retryCount >= 3) {
			log.error("태그 생성 최대 재시도 횟수 초과: {}", tagNames);
			return;
		}

		try {
			// 지수 백오프로 재시도
			Thread.sleep(100L * (int) Math.pow(2, retryCount));

			String values = tagNames.stream()
				.sorted()
				.map(tag -> String.format("('%s')", tag.replace("'", "''")))
				.collect(Collectors.joining(", "));

			String sql = "INSERT IGNORE INTO tags (tag_name) VALUES " + values;
			jdbcTemplate.update(sql);

		} catch (CannotAcquireLockException e) {
			handleDeadlock(tagNames, retryCount + 1);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
