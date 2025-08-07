package com.example.place.domain.mail;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AsyncConfig {
	// 필요 시, Executor 직접 설정도 가능
}
