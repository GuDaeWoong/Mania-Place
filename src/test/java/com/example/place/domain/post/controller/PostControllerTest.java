package com.example.place.domain.post.controller;

import com.example.place.domain.post.dto.request.PostCreateRequestDto;
import com.example.place.domain.post.dto.response.PostResponseDto;
import com.example.place.domain.post.service.PostService;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
class PostControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PostService postService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("게시글 생성 성공 테스트")
	void createPost_success() throws Exception {

		PostCreateRequestDto requestDto = new PostCreateRequestDto();
		requestDto.setUserId(1L);
		requestDto.setItemId(2L);
		requestDto.setContent("테스트 내용입니다.");
		requestDto.setImage("https://cdn.example.com/test.jpg");

		PostResponseDto responseDto = new PostResponseDto();
		responseDto.setId(10L);
		responseDto.setContent(requestDto.getContent());
		responseDto.setImage(requestDto.getImage());
		responseDto.setUserId(requestDto.getUserId());
		responseDto.setItemId(requestDto.getItemId());

		Mockito.when(postService.createPost(any())).thenReturn(responseDto);

		mockMvc.perform(post("/posts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestDto)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").value(10L))
			.andExpect(jsonPath("$.content").value("테스트 내용입니다."))
			.andExpect(jsonPath("$.image").value("https://cdn.example.com/test.jpg"))
			.andExpect(jsonPath("$.userId").value(1L))
			.andExpect(jsonPath("$.itemId").value(2L));
	}

	//단건 조회
	@GetMapping("/{id}")
	public ResponseEntity<PostResponseDto> getPostById(@PathVariable("id") Long id) {
		PostResponseDto response = postService.findPostById(id);
		return ResponseEntity.ok(response);
	}

}
