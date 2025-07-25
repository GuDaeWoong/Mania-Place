package com.example.place.domain.vote.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;
import com.example.place.domain.post.service.PostService;
import com.example.place.domain.post.entity.Post;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.user.service.UserService;
import com.example.place.domain.vote.dto.request.VoteRequestDto;
import com.example.place.domain.vote.dto.response.VoteResponseDto;
import com.example.place.domain.vote.entity.Vote;
import com.example.place.domain.vote.entity.VoteType;
import com.example.place.domain.vote.repository.VoteRepository;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VoteService {

	private final VoteRepository voteRepository;
	private final PostService postService;
	private final UserService userService;

	@Transactional
	public VoteResponseDto createVote(Long postId, VoteRequestDto request, Long userId) {
		User user = userService.findByIdOrElseThrow(userId);
		Post post = postService.findByIdOrElseThrow(postId);

		VoteType newVoteType = VoteType.valueOf(request.getVoteType());

		// 동일한 투표를 했는지 확인
		Optional<Vote> existingVote = voteRepository.findByUserAndPostAndVoteType(user, post, newVoteType);

		// 동일한 투표를 했다면 에러 반환
		if (existingVote.isPresent()) {
			throw new CustomException(ExceptionCode.ALREADY_VOTED);
		}

		// 투표 저장
		Vote vote = Vote.of(user, post, newVoteType);
		voteRepository.save(vote);

		// 응답값 조회
		Object[] result = voteRepository.findVoteStatsByPostIds(postId, userId).get(0);

		// like 개수
		Long likeCount = ((Number)result[0]).longValue();
		// dislike 개수
		Long dislikeCount = ((Number)result[1]).longValue();
		// 로그인한 유저의 like 투표 여부
		boolean isLike = ((Number)result[2]).intValue() == 1;
		// 로그인한 유저의 dislike 투표 여부
		boolean isDislike = ((Number)result[3]).intValue() == 1;

		return VoteResponseDto.of(likeCount, dislikeCount, isLike, isDislike);
	}

	public VoteResponseDto deleteVote(Long postId, @Valid VoteRequestDto request, Long userId) {
		User user = userService.findByIdOrElseThrow(userId);
		Post post = postService.findByIdOrElseThrow(postId);

		VoteType newVoteType = VoteType.valueOf(request.getVoteType());

		// 취소할 투표가 존재하는지 확인 (취소할 투표가 없다면 에러 반환)
		Vote existingVote = voteRepository.findByUserAndPostAndVoteType(user, post, newVoteType)
			.orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_VOTE));

		// 투표 삭제
		voteRepository.delete(existingVote);

		// 응답값 조회
		Object[] result = voteRepository.findVoteStatsByPostIds(postId, userId).get(0);

		// like 개수
		Long likeCount = ((Number)result[0]).longValue();
		// dislike 개수
		Long dislikeCount = ((Number)result[1]).longValue();
		// 로그인한 유저의 like 투표 여부
		boolean isLike = ((Number)result[2]).intValue() == 1;
		// 로그인한 유저의 dislike 투표 여부
		boolean isDislike = ((Number)result[3]).intValue() == 1;

		return VoteResponseDto.of(likeCount, dislikeCount, isLike, isDislike);
	}
}
