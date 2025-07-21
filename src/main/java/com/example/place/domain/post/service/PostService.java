package com.example.place.domain.post.service;

import org.springframework.stereotype.Service;

import com.example.place.domain.item.entity.Item;
import com.example.place.domain.item.repository.ItemRepository;
import com.example.place.domain.post.dto.PostCreateRequestDto;
import com.example.place.domain.post.dto.PostResponseDto;
import com.example.place.domain.post.entity.Post;
import com.example.place.domain.post.repository.PostRepository;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.user.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final ItemRepository itemRepository;

	public PostResponseDto createPost(PostCreateRequestDto request) {
		User user = userRepository.findById(request.getUserId())
			.orElseThrow(() -> new EntityNotFoundException("User not found"));

		Item item = itemRepository.findById(request.getItemId())
			.orElseThrow(() -> new EntityNotFoundException("Item not found"));

		Post post = new Post(user, item, request.getContent(), request.getImage());
		Post saved = postRepository.save(post);

		// builder 없이 수동으로 DTO 생성
		PostResponseDto response = new PostResponseDto();
		response.setId(saved.getId());
		response.setContent(saved.getContent());
		response.setImage(saved.getImage());
		response.setUserId(saved.getUser().getId());
		response.setItemId(saved.getItem().getId());
		return response;
	}
}


