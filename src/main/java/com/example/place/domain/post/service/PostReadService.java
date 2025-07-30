package com.example.place.domain.post.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.place.common.annotation.Loggable;
import com.example.place.common.dto.PageResponseDto;
import com.example.place.domain.Image.entity.Image;
import com.example.place.domain.Image.service.ImageService;
import com.example.place.domain.post.dto.response.PostSearchAllResponseDto;
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

	// 살까말까 전체 조회
	public Page<Post> getPostsPageOnly(Pageable pageable) {
		return postRepository.findAll(pageable);
	}

	// PostSearchAllResponseDto 페이지로 변환하는 메서드
	@Loggable
	public PageResponseDto<PostSearchAllResponseDto> getPostsWithVoteInfo(
		Pageable pageable,
		Long userId
	) {
		// PostRepository에서 게시글 엔티티 페이지를 반환
		Page<Post> postsPage = getPostsPageOnly(pageable);

		// 게시글 페이지에서 모든 게시글 ID 목록을 추출
		List<Long> postIds = postsPage.getContent().stream()
			.map(Post::getId)
			.toList();

		// 해당 게시글 ID 목록에 대한 투표 정보를 반환
		Map<Long, VoteResponseDto> voteMap = voteService.getVoteForPosts(postIds, userId);

		// Post,Vote 사용해서  PostSearchAllResponseDto로 변환
		Map<Long, List<Image>> itemIdToImagesMap = imageService.mapItemIdsToImagesFromPosts(postsPage);

		Page<PostSearchAllResponseDto> dtoPage = postsPage.map(post -> {
			Long postId = post.getId();
			Long itemId = post.getItem().getId();

			// 메인이미지
			String mainImageUrl = itemIdToImagesMap.getOrDefault(itemId, Collections.emptyList()).stream()
				.filter(Image::isMain)
				.findFirst()
				.map(Image::getImageUrl)
				.orElse(null);

			// 게시글의 투표 정보 반환
			VoteResponseDto voteInfo = voteMap.getOrDefault(postId, VoteResponseDto.empty());

			return PostSearchAllResponseDto.from(
				postId,
				post.getContent(),
				mainImageUrl,
				voteInfo.getLikeCount(),
				voteInfo.getDisLikeCount(),
				voteInfo.isLike(),
				voteInfo.isDislike()
			);
		});

		return new PageResponseDto<>(dtoPage);
	}

}