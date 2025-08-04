package com.example.place.domain.post.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.place.common.annotation.Loggable;
import com.example.place.common.dto.PageResponseDto;
import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;
import com.example.place.domain.Image.dto.ImageDto;
import com.example.place.domain.Image.entity.Image;
import com.example.place.domain.Image.service.ImageService;
import com.example.place.domain.post.dto.response.PostResponseDto;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.user.service.UserService;
import com.example.place.domain.item.entity.Item;
import com.example.place.domain.item.service.ItemService;
import com.example.place.domain.post.dto.request.PostCreateRequestDto;
import com.example.place.domain.post.dto.request.PostUpdateRequestDto;
import com.example.place.domain.post.entity.Post;
import com.example.place.domain.post.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

	private final PostRepository postRepository;
	private final UserService userService;
	private final ItemService itemService;
	private final ImageService imageService;

	//살까말까 생성
	@Loggable
	@Transactional
	public PostResponseDto createPost(Long userId, PostCreateRequestDto request) {
		User user = userService.findByIdOrElseThrow(userId);
		Item item = itemService.findByIdOrElseThrow(request.getItemId());

		Post post = Post.of(user, item, request.getContent());
		Post saved = postRepository.save(post);

		ImageDto imageDto = imageService.getImages(saved.getItem().getId());
		return PostResponseDto.from(saved, imageDto);
	}

	//살까말까 단건 조회
	@Loggable
	@Transactional(readOnly = true)
	public PostResponseDto getPost(Long postId) {
		Post post = findByIdOrElseThrow(postId);

		ImageDto imageDto = imageService.getImages(post.getItem().getId());
		return PostResponseDto.from(post, imageDto);
	}

	//살까말까 수정
	@Loggable
	@Transactional
	public PostResponseDto updatePost(Long postId, PostUpdateRequestDto request, Long userId) {
		Post post = findByIdOrElseThrow(postId);

		if (!post.getUser().getId().equals(userId)) {
			throw new CustomException(ExceptionCode.FORBIDDEN_POST_ACCESS);
		}

		post.update(request.getContent());
		ImageDto imageDto = imageService.getImages(post.getItem().getId());
		return PostResponseDto.from(post, imageDto);
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
		return postRepository.findByIdAndIsDeletedFalse(id)
			.orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_POST));
	}



	@Loggable
	@Transactional
	public void softDeletePost(Long postId, Long userId) {
		Post post = findByIdOrElseThrow(postId);

		if (!post.getUser().getId().equals(userId)) {
			throw new CustomException(ExceptionCode.FORBIDDEN_POST_DELETE);
		}
		post.delete();
	}

	@Transactional
	public void softAllDeletePost(Long itemId) {
		List<Post> posts = postRepository.findByItemId(itemId);

		for (Post post : posts) {
			post.delete();
		}
	}
}