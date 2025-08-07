package com.example.place.domain.mail;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MailController {

	private final MailService mailService;

	@PostMapping("/api/mail")
	public void sendMailTest() {
		List<String> emailList = List.of(
			"woosaevit@gmail.com"
			// "day7of01@gmail.com",
			// "ghwns2239@gmail.com",
			// "elpin0428@naver.com",
			// "eye6246@gmail.com"
		);

		for (String email : emailList) {
			mailService.sendNewsfeedNotification(email, "테스트입니다.");
		}
	}
}
