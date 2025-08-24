package com.example.place.domain.mail.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
class MailSendServiceTest {

	private static final String TEST_EMAIL = "test@email.com";
	private static final String TEST_NEWSFEED_TILE = "testNewsfeedTitle";

	@Mock
	private JavaMailSender mailSender;

	@InjectMocks
	private MailSendService mailSendService;

	@Captor
	ArgumentCaptor<SimpleMailMessage> mailCaptor;

	@Test
	void 메일_전송_성공() {
		// when
		mailSendService.sendNewsfeedNotification(TEST_EMAIL, TEST_NEWSFEED_TILE);

		// then
		verify(mailSender, times(1)).send(mailCaptor.capture());
		SimpleMailMessage sentMessage = mailCaptor.getValue();
		assertEquals(TEST_EMAIL, sentMessage.getTo()[0]);
		;
		assertTrue(sentMessage.getText().contains(TEST_NEWSFEED_TILE));
	}

}
