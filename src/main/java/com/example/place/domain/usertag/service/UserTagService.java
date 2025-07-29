package com.example.place.domain.usertag.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.place.domain.user.entity.User;
import com.example.place.domain.usertag.repository.UserTagRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserTagService {
	private final UserTagRepository userTagRepository;

	public void saveUserTags(User user, List<Long> tagIds) {
		userTagRepository.bulkInsertUserTags(user.getId(), tagIds);
	}
}
