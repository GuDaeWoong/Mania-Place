package com.example.place.domain.post.service;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.place.common.annotation.Loggable;
import com.example.place.common.dto.PageResponseDto;
import com.example.place.domain.Image.entity.Image;
import com.example.place.domain.Image.service.ImageService;
import com.example.place.domain.post.dto.response.PostGetAllResponseDto;
import com.example.place.domain.post.entity.Post;
import com.example.place.domain.post.repository.PostRepository;
import com.example.place.domain.vote.dto.response.VoteResponseDto;
import com.example.place.domain.vote.service.VoteService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostReadService {

	private final PostRepository postRepository;
	private final ImageService imageService;
	private final VoteService voteService;

	// PostSearchAllResponseDto 페이지로 변환하는 메서드
	@Loggable
	@Transactional(readOnly = true)
	public PageResponseDto<PostGetAllResponseDto> getAllPosts(Pageable pageable, Long userId) {
		// PostRepository에서 게시글 엔티티 페이지를 조회
		Page<Post> pagedPosts = postRepository.findAll(pageable);

		return buildGetALLPosts(pagedPosts, userId);
	}

	// 살까말까 내 글 조회
	@Loggable
	@Transactional(readOnly = true)
	public PageResponseDto<PostGetAllResponseDto> getMyPosts(Long userId, Pageable pageable) {
		// PostRepository에서 게시글 엔티티 페이지를 조회
		Page<Post> pagedPosts = postRepository.findAllByUserAndIsDeletedFalse(userId, pageable);

		return buildGetALLPosts(pagedPosts, userId);
	}

	// 전체 조회 빌더 (공통 로직 메서드)
	@Loggable
	@Transactional(readOnly = true)
	private PageResponseDto<PostGetAllResponseDto> buildGetALLPosts(Page<Post> pagedPosts, Long userId) {

		// 해당 게시글 ID 목록에 대한 이미지 정보를 반환
		Map<Long, Image> imagesMap = imageService.getMainImagesForPosts(pagedPosts);

		// 해당 게시글 ID 목록에 대한 투표 정보를 반환
		Map<Long, VoteResponseDto> voteMap = voteService.getVotesForPosts(pagedPosts, userId);

		// 조합
		Page<PostGetAllResponseDto> dtoPage = pagedPosts.map(post -> {
			Long postId = post.getId();
			Long itemId = post.getItem().getId();

			// --메인이미지 조합
			Image mainImage = imagesMap.getOrDefault(itemId, null);

			// --투표 정보 조합
			VoteResponseDto voteInfo = voteMap.getOrDefault(postId, VoteResponseDto.empty());

			return PostGetAllResponseDto.from(
				postId,
				post.getContent(),
				mainImage.getImageUrl(),
				voteInfo.getLikeCount(),
				voteInfo.getDisLikeCount(),
				voteInfo.isLike(),
				voteInfo.isDislike()
			);
		});

		return new PageResponseDto<>(dtoPage);
	}

}