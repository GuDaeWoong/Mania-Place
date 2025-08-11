package com.example.place.domain.mail.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.place.domain.mail.dto.MailRequest;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailRequestService {

	private final RedisMailQueueService redisMailQueueService;
	private final UserService userService;

	public void enqueueNewsfeedEmailToAllUsers(String newsfeedTitle) {

		List<User> users = userService.findUserByIsDeletedFalse();

		for (User user : users) {
			redisMailQueueService.enqueueMail(MailRequest.of(user.getEmail(), newsfeedTitle));
		}
	}
}
