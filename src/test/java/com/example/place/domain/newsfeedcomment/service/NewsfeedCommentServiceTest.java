package com.example.place.domain.newsfeedcomment.service;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Arrays;
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
import com.example.place.domain.newsfeed.entity.Newsfeed;
import com.example.place.domain.newsfeed.service.NewsfeedService;
import com.example.place.domain.newsfeedcomment.dto.request.NewsfeedCommentRequest;
import com.example.place.domain.newsfeedcomment.dto.response.NewsfeedCommentResponse;
import com.example.place.domain.newsfeedcomment.entity.NewsfeedComment;
import com.example.place.domain.newsfeedcomment.repository.NewsfeedCommentRepository;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.user.entity.UserRole;
import com.example.place.domain.user.service.UserService;

@ExtendWith(MockitoExtension.class)
public class NewsfeedCommentServiceTest {

	private static final User TEST_USER = User.of(
		"testName",
		"testNickname",
		"test@email.com",
		"Test1234!",
		"url",
		UserRole.USER
	);

	private static final Newsfeed TEST_NEWSFEED = Newsfeed.of(
		TEST_USER,
		"testTitle",
		"testContent"
	);

	private static final NewsfeedComment TEST_COMMENT = NewsfeedComment.of(
		TEST_USER,
		TEST_NEWSFEED,
		"testContent"
	);

	static {
		ReflectionTestUtils.setField(TEST_USER, "id", 1L);
		ReflectionTestUtils.setField(TEST_NEWSFEED, "id", 1L);
		ReflectionTestUtils.setField(TEST_COMMENT, "id", 1L);
	}

	@Mock
	private NewsfeedService newsfeedService;

	@Mock
	private UserService userService;

	@Mock
	private NewsfeedCommentRepository newsfeedCommentRepository;

	@InjectMocks
	private NewsfeedCommentService newsfeedCommentService;

	@Test
	void 뉴스피드댓글_생성_성공() {
		// given
		Long newsfeedId = 1L;
		NewsfeedCommentRequest requestDto = new NewsfeedCommentRequest();
		ReflectionTestUtils.setField(requestDto, "content", "새 댓글");
		CustomPrincipal principal = new CustomPrincipal(TEST_USER.getId(),TEST_USER.getName(),TEST_USER.getNickname(), TEST_USER.getEmail(),
			List.of(new SimpleGrantedAuthority("ROLE_USER")));

		given(userService.findByIdOrElseThrow(principal.getId())).willReturn(TEST_USER);
		given(newsfeedService.findByIdOrElseThrow(newsfeedId)).willReturn(TEST_NEWSFEED);
		given(newsfeedCommentRepository.save(any(NewsfeedComment.class)))
			.willAnswer(invocation -> invocation.getArgument(0));

		// when
		NewsfeedCommentResponse responseDto = newsfeedCommentService.createNewsfeedComment(newsfeedId, requestDto, principal);

		// then
		assertThat(responseDto).isNotNull();
	}

	@Test
	void 뉴스피드댓글_전체조회_성공() {
		// given
		Long newsfeedId = 1L;
		Pageable pageable = PageRequest.of(0, 10);

		NewsfeedComment comment1 = NewsfeedComment.of(TEST_USER, TEST_NEWSFEED, "댓글1");
		NewsfeedComment comment2 = NewsfeedComment.of(TEST_USER, TEST_NEWSFEED, "댓글2");
		List<NewsfeedComment> commentList = Arrays.asList(comment1, comment2);
		Page<NewsfeedComment> commentPage = new PageImpl<>(commentList, pageable, commentList.size());

		given(newsfeedService.findByIdOrElseThrow(newsfeedId)).willReturn(TEST_NEWSFEED);
		given(newsfeedCommentRepository.findAllByNewsfeedAndIsDeletedFalse(TEST_NEWSFEED, pageable)).willReturn(commentPage);

		// when
		PageResponseDto<NewsfeedCommentResponse> responseDto = newsfeedCommentService.getAllCommentsByNewsfeeds(newsfeedId, pageable);

		// then
		assertThat(responseDto).isNotNull();
		assertThat(responseDto.getContent()).hasSize(2);
	}

	@Test
	void 뉴스피드댓글_수정_성공() {
		// given
		Long newsfeedId = 1L;
		Long commentId = 1L;
		NewsfeedCommentRequest requestDto = new NewsfeedCommentRequest();
		ReflectionTestUtils.setField(requestDto, "content", "수정된 댓글");
		CustomPrincipal principal = new CustomPrincipal(TEST_USER.getId(),TEST_USER.getName(),TEST_USER.getNickname(), TEST_USER.getEmail(),
			List.of(new SimpleGrantedAuthority("ROLE_USER")));

		given(newsfeedService.findByIdOrElseThrow(newsfeedId)).willReturn(TEST_NEWSFEED);
		given(userService.findByIdOrElseThrow(principal.getId())).willReturn(TEST_USER);
		given(newsfeedCommentRepository.findByIdAndIsDeletedFalse(commentId)).willReturn(Optional.of(TEST_COMMENT));

		// when
		NewsfeedCommentResponse responseDto = newsfeedCommentService.updateNewsfeedComment(newsfeedId, commentId, requestDto, principal);

		// then
		assertThat(responseDto).isNotNull();
	}
}
