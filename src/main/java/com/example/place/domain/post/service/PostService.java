package com.example.place.domain.post.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.place.common.dto.PageResponseDto;
import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;
import com.example.place.domain.post.dto.response.PostWithUserResponseDto;
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

	//살까말까 생성
	@Transactional
	public PostWithUserResponseDto createPost(Long userId, PostCreateRequestDto request) {
		User user = userService.findByIdOrElseThrow(userId);
		Item item = itemService.findByIdOrElseThrow(request.getItemId());

		Post post = Post.of(user, item, request.getContent());
		Post saved = postRepository.save(post);

		return PostWithUserResponseDto.from(saved);
	}

	//살까말까 단건 조회
	@Transactional(readOnly = true)
	public PostWithUserResponseDto getPost(Long postId) {
		Post post = findByIdOrElseThrow(postId);
		return PostWithUserResponseDto.from(post);
	}

	//살까말까 전체 조회
	@Transactional(readOnly = true)
	public PageResponseDto<PostWithUserResponseDto> getAllPosts(Pageable pageable) {

		Page<Post> postsPage = postRepository.findAll(pageable);

		Page<PostWithUserResponseDto> dtoPage = postsPage.map(PostWithUserResponseDto::from);

		return new PageResponseDto<>(dtoPage);
	}


	//살까말까 내 글 조회
	@Transactional(readOnly = true)
	public PageResponseDto<PostWithUserResponseDto> findMyPosts(Long userId, Pageable pageable) {
		User user = userService.findByIdOrElseThrow(userId);

		Page<Post> postsPage = postRepository.findAllByUser(user,pageable);

		Page<PostWithUserResponseDto> dtoPage = postsPage.map(PostWithUserResponseDto::from);

		return new PageResponseDto<>(dtoPage);
	}

	//살까말까 수정
	@Transactional
	public PostWithUserResponseDto updatePost(Long postId, PostUpdateRequestDto request, Long userId) {
		Post post = findByIdOrElseThrow(postId);

		if (!post.getUser().getId().equals(userId)) {
			throw new CustomException(ExceptionCode.FORBIDDEN_POST_ACCESS);
		}

		post.update(request.getContent());
		return PostWithUserResponseDto.from(post);
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
