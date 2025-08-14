package com.example.place.domain.mail.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class MailRequest implements Serializable {
	private String toEmail;
	private String title;
	private int retryCount = 0;  // 재시도 횟수 기본 0

	private MailRequest(String toEmail, String newsfeedTitle) {
		this.toEmail = toEmail;
		this.title = newsfeedTitle;
	}

	public static MailRequest of(String toEmail, String newsfeedTitle) {
		return new MailRequest(toEmail, newsfeedTitle);
	}
}
