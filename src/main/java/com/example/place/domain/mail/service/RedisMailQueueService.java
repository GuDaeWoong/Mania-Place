package com.example.place.domain.mail.service;

import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import com.example.place.domain.mail.dto.MailRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisMailQueueService {
	private final RedissonClient redissonClient;

	private static final String MAIL_QUEUE_KEY = "newsfeed:mail:queue";

	public void enqueueMail(MailRequest mailRequest) {
		RBlockingQueue<MailRequest> queue = redissonClient.getBlockingQueue(MAIL_QUEUE_KEY);
		queue.add(mailRequest);  // enqueue에 저장
	}
}
