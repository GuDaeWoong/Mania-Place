package com.example.place.domain.tag.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.example.place.domain.tag.entity.Tag;
import com.example.place.domain.tag.repository.TagRepository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagCreateService {
	private final TagRepository tagRepository;
	private final EntityManager entityManager;

	public Tag findOrCreateTag(String tagName) {
		Tag existingTag = tagRepository.findByTagName(tagName)
			.orElse(null);
		if (existingTag != null) {
			return existingTag;
		}
		try {
			Tag newTag = Tag.of(tagName);
			return tagRepository.save(newTag);
		} catch (DataIntegrityViolationException e) {
			// 3. 다른 트랜잭션에서 먼저 만들었을 경우 재조회
			entityManager.clear(); // 현재 session 비우기
			return tagRepository.findByTagName(tagName)
				.orElseThrow(() -> new IllegalStateException("태그 생성 실패 후 재조회 실패: " + tagName));
		}
	}
}
