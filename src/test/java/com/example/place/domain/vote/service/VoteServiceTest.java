package com.example.place.domain.vote.service;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;
import com.example.place.domain.item.entity.Item;
import com.example.place.domain.post.entity.Post;
import com.example.place.domain.post.service.PostService;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.user.entity.UserRole;
import com.example.place.domain.user.service.UserService;
import com.example.place.domain.vote.dto.request.VoteRequestDto;
import com.example.place.domain.vote.dto.response.VoteResponseDto;
import com.example.place.domain.vote.entity.Vote;
import com.example.place.domain.vote.entity.VoteType;
import com.example.place.domain.vote.repository.VoteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {

	private static final User TEST_USER = User.of(
		"testUser",
		"testNickname",
		"test@email.com",
		"Test1234!",
		"test.url",
		UserRole.USER
	);
	private static final Item TEST_ITEM = Item.of(
		TEST_USER,
		"testItem",
		"testDescription",
		100.0,
		1L,
		false,
		LocalDateTime.parse("2025-07-23T00:00:00"),
		LocalDateTime.parse("2125-07-23T00:00:00")
	);
	private static final Post TEST_POST = Post.of(
		TEST_USER,
		TEST_ITEM,
		"testContent"
	);

	@Mock
	private VoteRepository voteRepository;

	@Mock
	private PostService postService;

	@Mock
	private UserService userService;

	@InjectMocks
	private VoteService voteService;

	@Test
	void 투표_등록_성공() throws Exception {
		// given
		ReflectionTestUtils.setField(TEST_POST, "id", 1L);
		ReflectionTestUtils.setField(TEST_USER, "id", 1L);

		ObjectMapper mapper = new ObjectMapper();
		VoteRequestDto request = mapper.readValue("{\"voteType\":\"LIKE\"}", VoteRequestDto.class);

		when(userService.findByIdOrElseThrow(anyLong())).thenReturn(TEST_USER);
		when(postService.findByIdOrElseThrow(anyLong())).thenReturn(TEST_POST);
		when(voteRepository.findByUserAndPostAndVoteType(any(), any(), any())).thenReturn(Optional.empty());
		when(voteRepository.findVoteStatsByPostIds(anyLong(), anyLong()))
			.thenReturn(List.<Object[]>of(new Object[] {1L, 0L, 1, 0}));

		// when
		VoteResponseDto response = voteService.createVote(TEST_POST.getId(), request, TEST_USER.getId());

		// then
		verify(voteRepository, times(1)).save(any(Vote.class));
		assertEquals(1L, response.getLikeCount());
		assertEquals(0L, response.getDisLikeCount());
		assertTrue(response.isLike());
		assertFalse(response.isDislike());
	}

	@Test
	void 투표_등록_실패_이미_투표를_한_경우() throws Exception {
		// given
		ReflectionTestUtils.setField(TEST_POST, "id", 1L);
		ReflectionTestUtils.setField(TEST_USER, "id", 1L);

		ObjectMapper mapper = new ObjectMapper();
		VoteRequestDto request = mapper.readValue("{\"voteType\":\"LIKE\"}", VoteRequestDto.class);

		when(userService.findByIdOrElseThrow(anyLong())).thenReturn(TEST_USER);
		when(postService.findByIdOrElseThrow(anyLong())).thenReturn(TEST_POST);
		when(voteRepository.findByUserAndPostAndVoteType(any(), any(), any()))
			.thenReturn(Optional.of(Vote.of(TEST_USER, TEST_POST, VoteType.LIKE)));

		// when
		CustomException exception = assertThrows(CustomException.class, () ->
			voteService.createVote(TEST_POST.getId(), request, TEST_USER.getId())
		);

		// then
		assertEquals(ExceptionCode.ALREADY_VOTED, exception.getExceptionCode());
	}

	@Test
	void 투표_취소_성공() throws Exception {
		// given
		ReflectionTestUtils.setField(TEST_POST, "id", 1L);
		ReflectionTestUtils.setField(TEST_USER, "id", 1L);

		ObjectMapper mapper = new ObjectMapper();
		VoteRequestDto request = mapper.readValue("{\"voteType\":\"LIKE\"}", VoteRequestDto.class);

		when(userService.findByIdOrElseThrow(anyLong())).thenReturn(TEST_USER);
		when(postService.findByIdOrElseThrow(anyLong())).thenReturn(TEST_POST);
		when(voteRepository.findByUserAndPostAndVoteType(any(), any(), any()))
			.thenReturn(Optional.of(Vote.of(TEST_USER, TEST_POST, VoteType.LIKE)));
		when(voteRepository.findVoteStatsByPostIds(anyLong(), anyLong()))
			.thenReturn(List.<Object[]>of(new Object[] {0L, 0L, 0, 0}));

		// when
		VoteResponseDto response = voteService.deleteVote(TEST_POST.getId(), request, TEST_USER.getId());

		// then
		verify(voteRepository, times(1)).delete(any(Vote.class));
		assertEquals(0L, response.getLikeCount());
		assertEquals(0L, response.getDisLikeCount());
		assertFalse(response.isLike());
		assertFalse(response.isDislike());
	}

	@Test
	void 투표_취소_실패_투표가_존재하지_않는_경우() throws Exception {
		// given
		ReflectionTestUtils.setField(TEST_POST, "id", 1L);
		ReflectionTestUtils.setField(TEST_USER, "id", 1L);

		ObjectMapper mapper = new ObjectMapper();
		VoteRequestDto request = mapper.readValue("{\"voteType\":\"LIKE\"}", VoteRequestDto.class);

		when(userService.findByIdOrElseThrow(anyLong())).thenReturn(TEST_USER);
		when(postService.findByIdOrElseThrow(anyLong())).thenReturn(TEST_POST);
		when(voteRepository.findByUserAndPostAndVoteType(any(), any(), any())).thenReturn(Optional.empty());

		// when
		CustomException exception = assertThrows(CustomException.class, () ->
			voteService.deleteVote(TEST_POST.getId(), request, TEST_USER.getId()));

		// then
		assertEquals(ExceptionCode.NOT_FOUND_VOTE, exception.getExceptionCode());
	}

	@Test
	void 게시글_페이지_투표_맵_조회_성공() {
		// given
		ReflectionTestUtils.setField(TEST_USER, "id", 1L);
		ReflectionTestUtils.setField(TEST_POST, "id", 1L);
		Page<Post> pagedPosts = new PageImpl<>(List.of(TEST_POST));

		when(voteRepository.findVoteStatsByPostIdsAndUser(anyList(), anyLong()))
			.thenReturn(List.<Object[]>of(new Object[] {TEST_POST.getId(), 0L, 0L, 0, 0}));

		// when
		Map<Long, VoteResponseDto> response = voteService.getVotesForPosts(pagedPosts, TEST_USER.getId());

		// then
		VoteResponseDto dto = response.get(TEST_POST.getId());
		assertEquals(0L, dto.getLikeCount());
		assertEquals(0L, dto.getDisLikeCount());
		assertFalse(dto.isLike());
		assertFalse(dto.isDislike());
	}

	@Test
	void 게시글_빈페이지_투표_맵_조회_성공() {
		// given
		ReflectionTestUtils.setField(TEST_USER, "id", 1L);
		Page<Post> pagedPosts = new PageImpl<>(List.of());

		// when
		Map<Long, VoteResponseDto> result = voteService.getVotesForPosts(pagedPosts, TEST_USER.getId());

		// then
		assertThat(result).isEmpty();
		verify(voteRepository, never()).findVoteStatsByPostIdsAndUser(any(), any());
	}
}