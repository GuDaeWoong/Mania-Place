package com.example.place.domain.keyword.domain.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "keyword_snapshot")
public class KeywordSnapshot {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	private String keyword;

	@Column(nullable = false)
	private LocalDateTime snapshotTime;

	@Column(nullable = false)
	private Long count;

	private KeywordSnapshot(String keyword, LocalDateTime snapshotTime, Long count) {
		this.keyword = keyword;
		this.snapshotTime = snapshotTime;
		this.count = count;
	}

	public static KeywordSnapshot of(String keyword, LocalDateTime snapshotTime, Long count){
		return new KeywordSnapshot(keyword,snapshotTime,count);
	}
}