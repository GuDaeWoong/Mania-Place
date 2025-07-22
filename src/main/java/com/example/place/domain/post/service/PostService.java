package com.example.place.domain.post.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.place.domain.item.entity.Item;
import com.example.place.domain.item.repository.ItemRepository;
import com.example.place.domain.post.dto.request.PostCreateRequestDto;
import com.example.place.domain.post.dto.response.PostResponseDto;
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

	public PostResponseDto createPost(Long userId, PostCreateRequestDto request) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new EntityNotFoundException("User not found"));

		Item item = itemRepository.findById(request.getItemId())
			.orElseThrow(() -> new EntityNotFoundException("Item not found"));

		Post post = new Post(user, item, request.getContent(), request.getImage());
		Post saved = postRepository.save(post);

		PostResponseDto response = new PostResponseDto();
		response.setId(saved.getId());
		response.setContent(saved.getContent());
		response.setImage(saved.getImage());
		response.setUserId(saved.getUser().getId());
		response.setItemId(saved.getItem().getId());
		return response;
	}

	public PostResponseDto readPost(Long postId) {
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new EntityNotFoundException("해당 게시글이 존재하지 않습니다."));

		PostResponseDto response = new PostResponseDto();
		response.setId(post.getId());
		response.setContent(post.getContent());
		response.setImage(post.getImage());
		response.setUserId(post.getUser().getId());
		response.setItemId(post.getItem().getId());

		return response;
	}

	public Page<PostResponseDto> findAllPosts(Pageable pageable) {
		return postRepository.findAll(pageable)
			.map(post -> {
				PostResponseDto dto = new PostResponseDto();
				dto.setId(post.getId());
				dto.setContent(post.getContent());
				dto.setImage(post.getImage());
				dto.setUserId(post.getUser().getId());
				dto.setItemId(post.getItem().getId());
				return dto;
			});
	}

	public Page<PostResponseDto> findMyPosts(Long userId, Pageable pageable) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new EntityNotFoundException("User not found"));

		return postRepository.findAllByUser(user, pageable)
			.map(PostResponseDto::from);
	}

}


