package com.example.place.domain.post.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.place.common.dto.PageResponseDto;
import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;
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
	@Transactional
	public PostResponseDto createPost(Long userId, PostCreateRequestDto request) {
		User user = userService.findByIdOrElseThrow(userId);
		Item item = itemService.findByIdOrElseThrow(request.getItemId());

		Post post = Post.of(user, item, request.getContent());
		Post saved = postRepository.save(post);

		return PostResponseDto.from(saved);
	}

	//살까말까 단건 조회
	@Transactional(readOnly = true)
	public PostResponseDto getPost(Long postId) {
		Post post = findByIdOrElseThrow(postId);
		return PostResponseDto.from(post);
	}

	//살까말까 전체 조회
	@Transactional(readOnly = true)
	public PageResponseDto<PostResponseDto> getAllPosts(Pageable pageable) {

		Page<Post> postsPage = postRepository.findAll(pageable);

		// 현제 페이지의 게시글과 맵핑된 상품별로 이미지 리스트 생성
		Map<Long, List<Image>> itemIdToImagesMap = mapItemIdsToImagesFromPosts(postsPage);

		Page<PostResponseDto> dtoPage = postsPage.map(
			post -> PostResponseDto.fromWithImages(post, itemIdToImagesMap.get(post.getItem().getId())));

		return new PageResponseDto<>(dtoPage);
	}

	//살까말까 내 글 조회
	@Transactional(readOnly = true)
	public PageResponseDto<PostResponseDto> getMyPosts(Long userId, Pageable pageable) {
		User user = userService.findByIdOrElseThrow(userId);

		Page<Post> postsPage = postRepository.findAllByUser(user, pageable);

		// 현제 페이지의 게시글과 맵핑된 상품별로 이미지 리스트 생성
		Map<Long, List<Image>> itemIdToImagesMap = mapItemIdsToImagesFromPosts(postsPage);

		Page<PostResponseDto> dtoPage = postsPage.map(
			post -> PostResponseDto.fromWithImages(post, itemIdToImagesMap.get(post.getItem().getId())));

		return new PageResponseDto<>(dtoPage);
	}

	//살까말까 수정
	@Transactional
	public PostResponseDto updatePost(Long postId, PostUpdateRequestDto request, Long userId) {
		Post post = findByIdOrElseThrow(postId);

		if (!post.getUser().getId().equals(userId)) {
			throw new CustomException(ExceptionCode.FORBIDDEN_POST_ACCESS);
		}

		post.update(request.getContent());
		return PostResponseDto.from(post);
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

	// 현재 페이지에 있는 상품의 이미지들을 맵으로 묶어 반환
	private Map<Long, List<Image>> mapItemIdsToImagesFromPosts(Page<Post> postsPage) {
		// 현재 페이지에 존재하는 상품 리스트
		List<Long> itemIds = postsPage.stream()
			.map(post -> post.getItem().getId())
			.distinct()
			.collect(Collectors.toList());

		// 해당 상품들의 이미지들 조회
		List<Image> images = imageService.findByItemIds(itemIds);

		// 상품별로 이미지 리스트 생성
		Map<Long, List<Image>> itemIdToImagesMap = images.stream()
			.collect(Collectors.groupingBy(img -> img.getItem().getId()));

		return itemIdToImagesMap;
	}
}
