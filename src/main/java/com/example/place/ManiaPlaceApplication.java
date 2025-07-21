package com.example.place;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ManiaPlaceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManiaPlaceApplication.class, args);
	}

}
