package com.example.place.domain.mail;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {
	private final JavaMailSender mailSender;

	@Async
	public void sendNewsfeedNotification(String toEmail, String newsfeedTitle) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(toEmail);
		message.setSubject("[새소식] '" + newsfeedTitle + "'가 새로 올라왔어요!");

		StringBuilder body = new StringBuilder();
		body.append("안녕하세요, Mania Place입니다.\n\n");
		body.append("제목: ").append(newsfeedTitle).append("\n");
		body.append("지금 바로 Mania Place에 접속하고 새소식을 확인해보세요!");

		message.setText(body.toString());
		mailSender.send(message);
	}
}
