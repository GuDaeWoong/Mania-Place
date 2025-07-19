package com.example.place.common.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@JsonPropertyOrder({"timestamp", "status", "error", "message", "path"})
public class ErrorResponseDto {

	private LocalDateTime timestamp;

	private int status;

	private String error;

	private String message;

	private String path;

	public ErrorResponseDto(int status, String error, String message, String path) {
		this.timestamp = LocalDateTime.now();
		this.status = status;
		this.error = error;
		this.message = message;
		this.path = path;
	}
}