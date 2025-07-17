package com.example.place.domain.comment.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.place.domain.comment.service.CommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;
}
