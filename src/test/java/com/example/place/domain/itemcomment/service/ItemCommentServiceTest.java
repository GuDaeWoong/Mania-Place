package com.example.place.domain.itemcomment.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import com.example.place.common.dto.PageResponseDto;
import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;
import com.example.place.common.security.jwt.CustomPrincipal;
import com.example.place.domain.item.entity.Item;
import com.example.place.domain.item.service.ItemService;
import com.example.place.domain.itemcomment.dto.request.ItemCommentRequest;
import com.example.place.domain.itemcomment.dto.response.ItemCommentResponse;
import com.example.place.domain.itemcomment.entity.ItemComment;
import com.example.place.domain.itemcomment.repository.ItemCommentRepository;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.user.entity.UserRole;
import com.example.place.domain.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ItemCommentServiceTest {

	@Mock
	private ItemCommentRepository itemCommentRepository;
	@Mock
	private ItemService itemService;
	@Mock
	private UserService userService;

	@InjectMocks
	private ItemCommentService itemCommentService;

	private User testUser;
	private Item testItem;
	private CustomPrincipal testPrincipal;

	@Spy
	private ItemComment testComment;

	@BeforeEach
	void setUp() {
		// 테스트용 User 객체 생성 및 ID 세팅
		testUser = User.of("테스트유저", "testUser", "test@example.com", "password123", null, UserRole.USER);
		ReflectionTestUtils.setField(testUser, "id", 1L);

		// 테스트용 Item 객체 생성 및 ID 세팅
		testItem = Item.of(testUser, "테스트 상품", "상품 설명입니다.", 10000.0, 100L, false, LocalDateTime.now().minusDays(1),
			LocalDateTime.now().plusDays(10));
		ReflectionTestUtils.setField(testItem, "id", 10L);

		// 테스트용 ItemComment 객체 생성 및 Spy로 감싸기
		testComment = spy(ItemComment.of(testUser, testItem, "Original content"));
		ReflectionTestUtils.setField(testComment, "id", 100L);

		// 테스트용 CustomPrincipal 생성 (권한은 테스트에서 중요하지 않아 빈 리스트)
		testPrincipal = new CustomPrincipal(testUser.getId(), testUser.getName(), testUser.getNickname(),
			testUser.getEmail(), Collections.emptyList());
	}

	// 특정 댓글 조회 시 Mock 설정
	private void mockFindComment(ItemComment comment) {
		when(itemCommentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
	}

	// CustomException 발생 여부를 검증하는 헬퍼
	private void assertCustomException(Runnable action, ExceptionCode code) {
		CustomException ex = assertThrows(CustomException.class, action::run);
		assertEquals(code, ex.getExceptionCode());
	}

	@Nested
	@DisplayName("댓글 생성 테스트")
	class CreateComment {
		@Test
		@DisplayName("성공 - 올바른 요청")
		void shouldCreateComment_whenValidRequest() {
			// 요청 객체 생성 및 필드 설정
			ItemCommentRequest request = new ItemCommentRequest();
			ReflectionTestUtils.setField(request, "content", "New comment");

			// 서비스 Mock 설정: 유저, 상품 조회 및 댓글 저장
			when(userService.findByIdOrElseThrow(testPrincipal.getId())).thenReturn(testUser);
			when(itemService.findByIdOrElseThrow(testItem.getId())).thenReturn(testItem);
			when(itemCommentRepository.save(any(ItemComment.class))).thenReturn(testComment);

			// 실제 메서드 호출
			ItemCommentResponse response = itemCommentService.createItemComment(testItem.getId(), request,
				testPrincipal);

			// 결과 검증
			assertNotNull(response);
			assertEquals(testUser.getNickname(), response.getNickname());
			assertEquals("Original content", response.getContent());
			verify(itemCommentRepository).save(any(ItemComment.class));
		}
	}

	@Nested
	@DisplayName("댓글 조회 테스트")
	class GetComments {
		@Test
		@DisplayName("성공 - 페이지네이션 적용")
		void shouldReturnPagedComments_whenItemExists() {
			// 페이징 객체와 테스트 데이터 준비
			Pageable pageable = PageRequest.of(0, 10);
			List<ItemComment> comments = List.of(testComment);
			Page<ItemComment> commentPage = new PageImpl<>(comments, pageable, 1);

			// 서비스 Mock 설정
			when(itemService.findByIdOrElseThrow(testItem.getId())).thenReturn(testItem);
			when(itemCommentRepository.findByItemIdAndIsDeletedFalse(testItem.getId(), pageable)).thenReturn(
				commentPage);

			// 메서드 호출
			PageResponseDto<ItemCommentResponse> response = itemCommentService.getAllItemComments(testItem.getId(),
				pageable);

			// 결과 검증
			assertNotNull(response);
			assertEquals(1, response.getContent().size());
			assertEquals("Original content", response.getContent().get(0).getContent());
		}
	}

	@Nested
	@DisplayName("댓글 수정 테스트")
	class UpdateComment {
		@Test
		@DisplayName("성공 - 본인 댓글 수정")
		void shouldUpdateComment_whenAuthorEdits() {
			ItemCommentRequest request = new ItemCommentRequest();
			ReflectionTestUtils.setField(request, "content", "Updated content");

			// 댓글 조회 Mock
			mockFindComment(testComment);

			// 메서드 호출
			ItemCommentResponse response = itemCommentService.updateItemComment(testItem.getId(), testComment.getId(),
				request, testPrincipal);

			// 수정 검증: 댓글 내용 변경 메서드 호출 확인
			assertNotNull(response);
			assertEquals("Updated content", response.getContent());
			verify(testComment).updateItemComment("Updated content");
		}

		@Test
		@DisplayName("실패 - 잘못된 상품 ID 경로")
		void shouldThrowException_whenInvalidItemId() {
			ItemCommentRequest request = new ItemCommentRequest();
			ReflectionTestUtils.setField(request, "content", "Updated content");

			mockFindComment(testComment);

			// 잘못된 상품 ID일 경우 예외 발생 검증
			assertCustomException(
				() -> itemCommentService.updateItemComment(99L, testComment.getId(), request, testPrincipal),
				ExceptionCode.INVALID_PATH);
		}

		@Test
		@DisplayName("실패 - 작성자가 아님")
		void shouldThrowException_whenUserIsNotAuthor() {
			ItemCommentRequest request = new ItemCommentRequest();
			ReflectionTestUtils.setField(request, "content", "Updated content");

			// 다른 유저를 Principal로 생성
			CustomPrincipal anotherPrincipal = new CustomPrincipal(99L, "anotherUser", "anotherUser",
				"another@example.com", Collections.emptyList());
			mockFindComment(testComment);

			// 작성자가 아니면 접근 금지 예외 검증
			assertCustomException(
				() -> itemCommentService.updateItemComment(testItem.getId(), testComment.getId(), request,
					anotherPrincipal), ExceptionCode.FORBIDDEN_COMMENT_ACCESS);
		}
	}

	@Nested
	@DisplayName("댓글 삭제 테스트")
	class DeleteComment {
		@Test
		@DisplayName("성공 - Soft Delete")
		void shouldSoftDeleteComment_whenAuthorDeletes() {
			mockFindComment(testComment);

			// 소프트 삭제 호출
			itemCommentService.softDeleteItemComment(testItem.getId(), testComment.getId(), testPrincipal);

			// 댓글 내부 상태 변경 검증, DB 삭제 호출 없어야 함
			verify(testComment).delete();
			verify(itemCommentRepository, never()).deleteById(anyLong());
		}

		@Test
		@DisplayName("실패 - 작성자가 아님 (Soft Delete)")
		void shouldThrowException_whenNonAuthorTriesSoftDelete() {
			CustomPrincipal anotherPrincipal = new CustomPrincipal(99L, "anotherUser", "anotherUser",
				"another@example.com", Collections.emptyList());
			mockFindComment(testComment);

			assertCustomException(
				() -> itemCommentService.softDeleteItemComment(testItem.getId(), testComment.getId(), anotherPrincipal),
				ExceptionCode.FORBIDDEN_COMMENT_DELETE);
		}
	}

	@Nested
	@DisplayName("댓글 단일 조회 테스트")
	class FindById {
		@Test
		@DisplayName("성공 - 정상 댓글 조회")
		void shouldReturnComment_whenExistsAndNotDeleted() {
			mockFindComment(testComment);

			// Spy 객체 안전하게 Stub
			doReturn(false).when(testComment).isDeleted();

			ItemComment foundComment = itemCommentService.findByIdOrElseThrow(testComment.getId());

			// 조회 결과 검증
			assertNotNull(foundComment);
			assertEquals(testComment.getId(), foundComment.getId());
		}

		@Test
		@DisplayName("실패 - 댓글 없음")
		void shouldThrowException_whenCommentNotFound() {
			when(itemCommentRepository.findById(anyLong())).thenReturn(Optional.empty());

			assertCustomException(() -> itemCommentService.findByIdOrElseThrow(999L), ExceptionCode.NOT_FOUND_COMMENT);
		}

		@Test
		@DisplayName("실패 - 이미 삭제된 댓글")
		void shouldThrowException_whenCommentAlreadyDeleted() {
			mockFindComment(testComment);
			doReturn(true).when(testComment).isDeleted();

			assertCustomException(() -> itemCommentService.findByIdOrElseThrow(testComment.getId()),
				ExceptionCode.NOT_FOUND_COMMENT);
		}
	}
}