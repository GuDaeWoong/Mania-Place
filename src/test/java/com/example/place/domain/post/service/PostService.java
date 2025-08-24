package com.example.place.domain.post.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;
import com.example.place.domain.Image.dto.ImageDto;
import com.example.place.domain.Image.service.ImageService;
import com.example.place.domain.item.entity.Item;
import com.example.place.domain.item.service.ItemService;
import com.example.place.domain.post.dto.request.PostCreateRequestDto;
import com.example.place.domain.post.dto.request.PostUpdateRequestDto;
import com.example.place.domain.post.dto.response.PostResponseDto;
import com.example.place.domain.post.entity.Post;
import com.example.place.domain.post.repository.PostRepository;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.user.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

	@Mock
	private PostRepository postRepository;
	@Mock
	private UserService userService;
	@Mock
	private ItemService itemService;
	@Mock
	private ImageService imageService;

	@InjectMocks
	private PostService postService;

	private User user;
	private Item item;
	private Post post;
	private ImageDto imageDto;

	private static void setId(Object target, Long id) {
		ReflectionTestUtils.setField(target, "id", id);
	}

	@BeforeEach
	void setUp() {
		user = User.of("u", "nick", "u@u.com", "P@ssw0rd!", null, /*UserRole*/ null);
		item = Item.of(user, "item", "desc", 100.0, 1L, false, null, null);
		post = Post.of(user, item, "content");
		imageDto = ImageDto.of(List.of("a.jpg", "b.jpg"), 0);

		setId(user, 10L);
		setId(item, 20L);
		setId(post, 30L);
	}

	@Nested
	@DisplayName("post create")
	class CreatePost {

		@Test
		@DisplayName("성공: 유저/아이템 조회 -> 저장 -> 이미지 조회 -> DTO 반환")
		void create_success() {
			// given
			PostCreateRequestDto req = new PostCreateRequestDto();
			ReflectionTestUtils.setField(req, "itemId", item.getId());
			ReflectionTestUtils.setField(req, "content", "hello");

			when(userService.findByIdOrElseThrow(user.getId())).thenReturn(user);
			when(itemService.findByIdOrElseThrow(item.getId())).thenReturn(item);
			when(postRepository.save(any(Post.class))).thenAnswer(inv -> {
				Post p = inv.getArgument(0);
				// save 후 id가 부여
				setId(p, 100L);
				return p;
			});
			when(imageService.getItemImages(item.getId())).thenReturn(imageDto);

			// when
			PostResponseDto dto = postService.createPost(user.getId(), req);

			// then
			assertNotNull(dto);
			assertEquals("hello", dto.getContent());
			assertEquals("nick", dto.getNickname());
			assertEquals(item.getId(), dto.getItemId());
			assertEquals(List.of("a.jpg", "b.jpg"), dto.getImageUrls());
			assertEquals(0, dto.getMainIndex());

			verify(userService).findByIdOrElseThrow(user.getId());
			verify(itemService).findByIdOrElseThrow(item.getId());
			verify(postRepository).save(argThat(p ->
				p.getUser().equals(user) &&
					p.getItem().equals(item) &&
					"hello".equals(p.getContent())
			));
			verify(imageService).getItemImages(item.getId());
			verifyNoMoreInteractions(postRepository, userService, itemService, imageService);
		}
	}

	@Nested
	@DisplayName("post get")
	class GetPost {

		@Test
		@DisplayName("성공: 포스트 조회, 이미지 조회")
		void get_success() {
			when(postRepository.findByIdAndIsDeletedFalse(post.getId()))
				.thenReturn(Optional.of(post));
			when(imageService.getItemImages(item.getId())).thenReturn(imageDto);

			PostResponseDto dto = postService.getPost(post.getId());

			assertEquals(post.getContent(), dto.getContent());
			assertEquals("nick", dto.getNickname());
			assertEquals(item.getId(), dto.getItemId());
			assertEquals(List.of("a.jpg", "b.jpg"), dto.getImageUrls());
			assertEquals(0, dto.getMainIndex());

			verify(imageService).getItemImages(item.getId());
		}

		@Test
		@DisplayName("실패: 포스트 없음 -> NOT_FOUND_POST")
		void get_not_found() {
			when(postRepository.findByIdAndIsDeletedFalse(999L))
				.thenReturn(Optional.empty());

			CustomException ex = assertThrows(CustomException.class,
				() -> postService.getPost(999L));
			assertEquals(ExceptionCode.NOT_FOUND_POST, ex.getExceptionCode());
		}
	}

	@Nested
	@DisplayName("post update")
	class UpdatePost {

		@Test
		@DisplayName("성공: 작성자 동일 -> 내용 변경, 이미지 조회, DTO 반환")
		void update_success() {
			when(postRepository.findByIdAndIsDeletedFalse(post.getId()))
				.thenReturn(Optional.of(post));
			when(imageService.getItemImages(item.getId())).thenReturn(imageDto);

			PostUpdateRequestDto req = new PostUpdateRequestDto();
			ReflectionTestUtils.setField(req, "content", "updated");

			PostResponseDto dto = postService.updatePost(post.getId(), req, user.getId());

			assertEquals("updated", dto.getContent());
			assertEquals("nick", dto.getNickname());
			assertEquals(item.getId(), dto.getItemId());
			assertEquals(List.of("a.jpg", "b.jpg"), dto.getImageUrls());
			assertEquals(0, dto.getMainIndex());

			verify(imageService).getItemImages(item.getId());
		}

		@Test
		@DisplayName("실패: 작성자 불일치 -> FORBIDDEN_POST_ACCESS")
		void update_forbidden() {
			when(postRepository.findByIdAndIsDeletedFalse(post.getId()))
				.thenReturn(Optional.of(post));

			PostUpdateRequestDto req = new PostUpdateRequestDto();
			ReflectionTestUtils.setField(req, "content", "updated");

			Long otherUserId = 999L;
			CustomException ex = assertThrows(CustomException.class,
				() -> postService.updatePost(post.getId(), req, otherUserId));
			assertEquals(ExceptionCode.FORBIDDEN_POST_ACCESS, ex.getExceptionCode());
			verify(imageService, never()).getItemImages(anyLong());
		}
	}

	@Nested
	@DisplayName("post softDelete")
	class SoftDeletePost {

		@Test
		@DisplayName("성공: 작성자 동일 -> post 소프트딜리트")
		void soft_delete_success() {
			when(postRepository.findByIdAndIsDeletedFalse(post.getId()))
				.thenReturn(Optional.of(post));

			postService.softDeletePost(post.getId(), user.getId());

			Boolean isDeleted = (Boolean)ReflectionTestUtils.getField(post, "isDeleted");
			assertTrue(isDeleted != null && isDeleted);
			verify(postRepository, never()).delete(any());
		}

		@Test
		@DisplayName("실패: 작성자 불일치 -> FORBIDDEN_POST_DELETE")
		void soft_delete_forbidden() {
			when(postRepository.findByIdAndIsDeletedFalse(post.getId()))
				.thenReturn(Optional.of(post));

			CustomException ex = assertThrows(CustomException.class,
				() -> postService.softDeletePost(post.getId(), 999L));
			assertEquals(ExceptionCode.FORBIDDEN_POST_DELETE, ex.getExceptionCode());
		}
	}

	@Nested
	@DisplayName("post softAllDelete")
	class SoftAllDeletePost {

		@Test
		@DisplayName("성공: itemId의 모든 포스트에 소프트딜리트")
		void soft_all_delete_success() {
			Post p1 = Post.of(user, item, "c1");
			setId(p1, 101L);
			Post p2 = Post.of(user, item, "c2");
			setId(p2, 102L);

			when(postRepository.findByItemId(item.getId()))
				.thenReturn(List.of(p1, p2));

			postService.softAllDeletePost(item.getId());

			Boolean d1 = (Boolean)ReflectionTestUtils.getField(p1, "isDeleted");
			Boolean d2 = (Boolean)ReflectionTestUtils.getField(p2, "isDeleted");
			assertTrue(Boolean.TRUE.equals(d1));
			assertTrue(Boolean.TRUE.equals(d2));

			verify(postRepository).findByItemId(item.getId());
			verifyNoMoreInteractions(postRepository);
		}
	}

	@Nested
	@DisplayName("findByIdOrElseThrow")
	class FindById {

		@Test
		@DisplayName("실패: 존재하지 않으면 NOT_FOUND_POST")
		void not_found() {
			when(postRepository.findByIdAndIsDeletedFalse(123L))
				.thenReturn(Optional.empty());

			CustomException ex = assertThrows(CustomException.class,
				() -> postService.findByIdOrElseThrow(123L));
			assertEquals(ExceptionCode.NOT_FOUND_POST, ex.getExceptionCode());
		}
	}
}
