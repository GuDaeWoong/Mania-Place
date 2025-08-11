package com.example.place.domain.mail.service;

import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.example.place.domain.mail.dto.MailRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisMailWorker implements InitializingBean, DisposableBean {
	private final RedissonClient redissonClient;
	private final MailSendService mailService;

	private static final String MAIL_QUEUE_KEY = "newsfeed:mail:queue";
	private volatile boolean running = true;
	private Thread workerThread;

	@Override
	public void afterPropertiesSet() {
		workerThread = new Thread(() -> {
			RBlockingQueue<MailRequest> queue = redissonClient.getBlockingQueue(MAIL_QUEUE_KEY);

			while (running) {
				MailRequest mailRequest = null;
				try {
					// 큐에서 MailRequest 객체가 들어올 때까지 대기
					mailRequest = queue.take();

					// 받은 요청으로 메일 전송
					mailService.sendNewsfeedNotification(
						mailRequest.getToEmail(),
						mailRequest.getTitle()
					);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt(); // 인터럽트 처리
				} catch (Exception e) {
					// 메일 전송 에러 발생 시 로그 기록
					log.error("[{}] {} - {} - (USER ID: {}) ({} ms) - ERROR: {} | Params: {}",
						"MAIL_SEND", MAIL_QUEUE_KEY, "sendNewsfeedNotification",
						"system", 0L, e.getMessage(),
						mailRequest != null ? mailRequest.toString() : "null"
					);

					if (mailRequest != null && mailRequest.getRetryCount() < 3) {
						mailRequest.setRetryCount(mailRequest.getRetryCount() + 1);
						try {
							// 실패한 요청 다시 큐에 넣기
							Thread.sleep(1000); // 1초 대기
							queue.offer(mailRequest);

						} catch (Exception ex) {
							// 큐에 다시 넣는 중 에러 발생 시 로그 기록
							log.error("[{}] {} - {} - (USER ID: {}) ({} ms) - ERROR: {} | Params: {}",
								"MAIL_SEND_REQUEUE", MAIL_QUEUE_KEY, "queue.offer",
								"system", 0L, ex.getMessage(),
								mailRequest != null ? mailRequest.toString() : "null"
							);
						}
					}
				}
			}
		});

		workerThread.setDaemon(true);
		workerThread.start();
	}

	@Override
	public void destroy() {
		running = false;
		workerThread.interrupt();
	}
}
