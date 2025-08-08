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

		// 실제 로직
		List<User> users = userService.findUserByIsDeletedFalse();

		for (User user : users) {
			redisMailQueueService.enqueueMail(MailRequest.of(user.getEmail(), newsfeedTitle));
		}

		// 개발 단계에서만 사용
		// List<String> usersEmail = List.of(
		// 	"woosaevit@gmail.com",
		// 	"day7of01@gmail.com",
		// 	"ghwns2239@gmail.com",
		// 	"elpin0428@naver.com",
		// 	"eye6246@gmail.com"
		// );
		//
		// for (String email : usersEmail) {
		// 	redisMailQueueService.enqueueMail(MailRequest.of(email, newsfeedTitle));
		// }

	}
}
