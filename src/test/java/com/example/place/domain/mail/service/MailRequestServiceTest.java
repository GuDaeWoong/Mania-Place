package com.example.place.domain.mail.service;

import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.place.domain.mail.dto.MailRequest;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.user.entity.UserRole;
import com.example.place.domain.user.service.UserService;

@ExtendWith(MockitoExtension.class)
class MailRequestServiceTest {

	private static final User TEST_USER = User.of(
		"testUser",
		"testNickname",
		"test@email.com",
		"Test1234!",
		"test.url",
		UserRole.USER
	);
	private static final String TEST_EMAIL = "test@email.com";
	private static final String TEST_NEWSFEED_TILE = "testNewsfeedTitle";

	@Mock
	private RedisMailQueueService redisMailQueueService;

	@Mock
	private UserService userService;

	@InjectMocks
	private MailRequestService mailRequestService;

	@Test
	void 회원들_메일_데이터_인큐_성공() {
		// given
		when(userService.findUserByIsDeletedFalse()).thenReturn(List.of(TEST_USER));

		// when
		mailRequestService.enqueueNewsfeedEmailToAllUsers(TEST_NEWSFEED_TILE);

		// then
		verify(redisMailQueueService, times(1))
			.enqueueMail(MailRequest.of(TEST_EMAIL, TEST_NEWSFEED_TILE));
	}

}