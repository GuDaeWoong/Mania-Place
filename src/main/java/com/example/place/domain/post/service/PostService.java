package com.example.place.domain.post.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.user.service.UserService;
import com.example.place.domain.item.entity.Item;
import com.example.place.domain.item.service.ItemService;
import com.example.place.domain.post.dto.request.PostCreateRequestDto;
import com.example.place.domain.post.dto.request.PostUpdateRequestDto;
import com.example.place.domain.post.dto.response.PostResponseDto;
import com.example.place.domain.post.entity.Post;
import com.example.place.domain.post.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

	private final PostRepository postRepository;
	private final UserService userService;
	private final ItemService itemService;

	//살까말까 생성
	public PostResponseDto createPost(Long userId, PostCreateRequestDto request) {
		User user = userService.findUserById(userId);
		Item item = itemService.findByIdOrElseThrow(request.getItemId());

		Post post = new Post(user, item, request.getContent(), request.getImage());
		Post saved = postRepository.save(post);

		return new PostResponseDto(saved);
	}

	//살까말까 단건 조회
	public PostResponseDto readPost(Long postId) {
		Post post = findByIdOrElseThrow(postId);
		return new PostResponseDto(post);
	}

	//살까말까 전체 조회
	public Page<PostResponseDto> getAllPosts(Pageable pageable) {
		return postRepository.findAll(pageable)
			.map(PostResponseDto::new);
	}

	//살까말까 내 글 조회
	public Page<PostResponseDto> findMyPosts(Long userId, Pageable pageable) {
		User user = userService.findUserById(userId);

		return postRepository.findAllByUser(user, pageable)
			.map(PostResponseDto::new);
	}

	//살까말까 수정
	@Transactional
	public PostResponseDto updatePost(Long postId, PostUpdateRequestDto request, Long userId) {
		Post post = findByIdOrElseThrow(postId);

		if (!post.getUser().getId().equals(userId)) {
			throw new CustomException(ExceptionCode.FORBIDDEN_POST_ACCESS);
		}

		post.update(request.getContent(), request.getImage());
		return new PostResponseDto(post);
	}

	//살까말까 삭제
	@Transactional
	public void deletePost(Long postId, Long userId) {
		Post post = findByIdOrElseThrow(postId);

		if (!post.getUser().getId().equals(userId)) {
			throw new CustomException(ExceptionCode.FORBIDDEN_POST_DELETE);
		}

		postRepository.delete(post);
	}

	public Post findByIdOrElseThrow(Long id) {
		return postRepository.findById(id)
			.orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_POST));
	}
}


