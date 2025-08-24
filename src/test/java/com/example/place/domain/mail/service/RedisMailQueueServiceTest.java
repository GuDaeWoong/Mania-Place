package com.example.place.domain.mail.service;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;

import com.example.place.domain.mail.dto.MailRequest;

@ExtendWith(MockitoExtension.class)
class RedisMailQueueServiceTest {
	
	private static final MailRequest TEST_MAILE_REQUEST = MailRequest.of("test@email.com", "testNewsfeedTitle");

	@Mock
	private RedissonClient redissonClient;

	@Mock
	private RBlockingQueue<Object> queue;

	@InjectMocks
	private RedisMailQueueService redisMailQueueService;

	@Test
	void 레디스큐_인큐_성공() {
		// given
		when(redissonClient.getBlockingQueue(anyString())).thenReturn(queue);

		// when
		redisMailQueueService.enqueueMail(TEST_MAILE_REQUEST);

		// then
		verify(queue, times(1)).add(TEST_MAILE_REQUEST);
	}

}