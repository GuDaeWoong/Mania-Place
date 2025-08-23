package com.example.place.domain.postcomment.service;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.place.common.dto.PageResponseDto;
import com.example.place.common.security.jwt.CustomPrincipal;
import com.example.place.domain.item.entity.Item;
import com.example.place.domain.post.entity.Post;
import com.example.place.domain.post.service.PostService;
import com.example.place.domain.postcomment.dto.request.PostCommentRequestDto;
import com.example.place.domain.postcomment.dto.response.PostCommentResponseDto;
import com.example.place.domain.postcomment.entity.PostComment;
import com.example.place.domain.postcomment.repository.PostCommentRepository;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.user.entity.UserRole;
import com.example.place.domain.user.service.UserService;

@ExtendWith(MockitoExtension.class)
public class PostCommnetServiceTest {

	private static final User TEST_USER = User.of(
		"testName",
		"testNickname",
		"test@email.com",
		"Test1234!",
		"url",
		UserRole.USER
	);

	private static final Item TEST_ITEM = Item.of(
		TEST_USER,
		"testItem",
		"testDescription",
		20000.0,
		2L,
		true,
		LocalDateTime.parse("2025-07-01T00:00:00"),
		LocalDateTime.parse("2026-07-01T00:00:00")
	);

	private static final Post TEST_POST = Post.of(
		TEST_USER,
		TEST_ITEM,
		"testContent"
	);

	private static final PostComment TEST_COMMENT = PostComment.of(
		TEST_USER,
		TEST_POST,
		"testComment"
	);

	static {
		ReflectionTestUtils.setField(TEST_USER, "id", 1L);
		ReflectionTestUtils.setField(TEST_ITEM, "id", 1L);
		ReflectionTestUtils.setField(TEST_POST, "id", 1L);
		ReflectionTestUtils.setField(TEST_COMMENT, "id", 1L);
	}

	@Mock
	private PostCommentRepository postCommentRepository;

	@Mock
	private PostService postService;

	@Mock
	private UserService userService;

	@InjectMocks
	private PostCommnetService postCommnetService;

	@Test
	void 댓글_생성_성공() {
		// given
		Long postId = 1L;
		PostCommentRequestDto requestDto = new PostCommentRequestDto();
		ReflectionTestUtils.setField(requestDto, "content", "새 댓글");
		CustomPrincipal principal = new CustomPrincipal(TEST_USER.getId(),TEST_USER.getName(),TEST_USER.getNickname(), TEST_USER.getEmail(),
			List.of(new SimpleGrantedAuthority("ROLE_USER")));

		given(userService.findByIdOrElseThrow(principal.getId())).willReturn(TEST_USER);
		given(postService.findByIdOrElseThrow(postId)).willReturn(TEST_POST);
		given(postCommentRepository.save(any(PostComment.class)))
			.willAnswer(invocation -> invocation.getArgument(0));

		// when
		PostCommentResponseDto responseDto = postCommnetService.createPostComment(postId, requestDto, principal);

		// then
		assertThat(responseDto).isNotNull();
		assertThat(responseDto.getContent()).isEqualTo("새 댓글");
	}

	@Test
	void 댓글_수정_성공() {
		// given
		Long postId = 1L;
		Long commentId = 1L;
		PostCommentRequestDto requestDto = new PostCommentRequestDto();
		ReflectionTestUtils.setField(requestDto, "content", "수정된 댓글");
		CustomPrincipal principal = new CustomPrincipal(TEST_USER.getId(),TEST_USER.getName(),TEST_USER.getNickname(), TEST_USER.getEmail(),
			List.of(new SimpleGrantedAuthority("ROLE_USER")));

		given(postService.findByIdOrElseThrow(postId)).willReturn(TEST_POST);
		given(userService.findByIdOrElseThrow(principal.getId())).willReturn(TEST_USER);
		given(postCommentRepository.findByIdAndIsDeletedFalse(commentId)).willReturn(Optional.of(TEST_COMMENT));

		// when
		PostCommentResponseDto responseDto = postCommnetService.updatePostComment(postId, commentId, requestDto, principal);

		// then
		assertThat(responseDto).isNotNull();
		assertThat(responseDto.getContent()).isEqualTo("수정된 댓글");
	}

	@Test
	void 게시글의_모든_댓글_조회_성공() {
		// given
		Long postId = 1L;
		Pageable pageable = PageRequest.of(0, 10);

		PostComment comment1 = PostComment.of(TEST_USER, TEST_POST, "댓글1");
		PostComment comment2 = PostComment.of(TEST_USER, TEST_POST, "댓글2");
		List<PostComment> commentList = Arrays.asList(comment1, comment2);
		Page<PostComment> commentPage = new PageImpl<>(commentList, pageable, commentList.size());

		given(postService.findByIdOrElseThrow(postId)).willReturn(TEST_POST);
		given(postCommentRepository.findAllByPostAndIsDeletedFalse(TEST_POST, pageable)).willReturn(commentPage);

		// when
		PageResponseDto<PostCommentResponseDto> responseDto = postCommnetService.getAllCommentsByPosts(postId, pageable);

		// then
		assertThat(responseDto).isNotNull();
		assertThat(responseDto.getContent()).hasSize(2);
	}

	@Test
	void 댓글_소프트삭제_성공() {
		// given
		Long postId = 1L;
		Long commentId = 1L;
		Long userId = 1L;

		given(postService.findByIdOrElseThrow(postId)).willReturn(TEST_POST);
		given(postCommentRepository.findByIdAndIsDeletedFalse(commentId)).willReturn(Optional.of(TEST_COMMENT));

		// when
		postCommnetService.softDeletePostComment(postId, commentId, userId);

		// then
		verify(postCommentRepository).findByIdAndIsDeletedFalse(commentId);
	}

	@Test
	void 게시글의_모든_댓글_소프트삭제_성공() {
		// given
		Long postId = 1L;

		PostComment comment1 = PostComment.of(TEST_USER, TEST_POST, "댓글1");
		PostComment comment2 = PostComment.of(TEST_USER, TEST_POST, "댓글2");
		List<PostComment> commentList = Arrays.asList(comment1, comment2);

		given(postCommentRepository.findByPostIdAndIsDeletedFalse(postId)).willReturn(commentList);

		// when
		postCommnetService.softDeleteAllPostComment(postId);

		// then
		verify(postCommentRepository).findByPostIdAndIsDeletedFalse(postId);
	}

}
