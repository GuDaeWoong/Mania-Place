package com.example.place.domain.mail.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.place.domain.mail.dto.ManualEmailRequestDto;
import com.example.place.domain.mail.service.MailRequestService;
import com.example.place.domain.mail.service.MailSendService;
import com.example.place.domain.mail.service.RedisMailQueueService;
import com.example.place.domain.mail.dto.MailRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MailController {

	private final MailRequestService mailRequestService;

	@PostMapping("/api/admin/mail")
	public void manualNewsfeedEmailToAllUsers(@RequestBody ManualEmailRequestDto request) {

		mailRequestService.enqueueNewsfeedEmailToAllUsers(request.getTitle());
	}
}
