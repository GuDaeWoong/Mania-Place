package com.example.place.domain.vote.service;

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

import aj.org.objectweb.asm.commons.GeneratorAdapter;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VoteService {

	private final VoteRepository voteRepository;
	private final PostService postService;
	private final UserService userService;

	@Transactional
	public VoteResponseDto vote(Long postId, VoteRequestDto request, Long userId) {
		User user = userService.findUserById(userId);
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

		// like 개수
		Long likeCount = voteRepository.countByPostAndVoteType(post, VoteType.LIKE);
		// dislike 개수
		Long dislikeCount = voteRepository.countByPostAndVoteType(post, VoteType.DISLIKE);

		return VoteResponseDto.of(likeCount, dislikeCount);
	}
}
