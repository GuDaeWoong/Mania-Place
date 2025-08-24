package com.example.place.domain.newsfeed.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.place.domain.Image.dto.ImageDto;
import com.example.place.domain.Image.entity.Image;
import com.example.place.domain.Image.service.ImageService;
import com.example.place.domain.mail.service.MailRequestService;
import com.example.place.domain.newsfeed.dto.request.NewsfeedRequest;
import com.example.place.domain.newsfeed.dto.response.NewsfeedResponse;
import com.example.place.domain.newsfeed.dto.response.NewsfeedListResponse;
import com.example.place.domain.newsfeed.entity.Newsfeed;
import com.example.place.domain.newsfeed.repository.NewsfeedRepository;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.user.entity.UserRole;
import com.example.place.domain.user.service.UserService;
import com.example.place.common.dto.PageResponseDto;

@ExtendWith(MockitoExtension.class)
public class NewsfeedServiceTest {

	private static final User TEST_USER = User.of(
		"testUser",
		"testNickname",
		"test@email.com",
		"Test1234!",
		null,
		UserRole.USER
	);

	private ImageDto TEST_IMAGEDTO = ImageDto.of(
		List.of("img1.jpg", "img2.jpg"),
		0
	);

	@Mock
	private NewsfeedRepository newsfeedRepository;
	@Mock
	private UserService userService;
	@Mock
	private ImageService imageService;
	@Mock
	private MailRequestService mailRequestService;
	@Mock
	private RedisTemplate<String, String> redisTemplate;

	@Spy
	@InjectMocks
	private NewsfeedService newsfeedService; // Spy로 만들어서 evictListCache 무시 가능

	@Captor
	ArgumentCaptor<Newsfeed> newsfeedCaptor;

	@BeforeEach
	void setUp() {
		lenient().doNothing().when(newsfeedService).evictListCache();
	}

	@Test
	void 새소식_등록_성공() {
		// given
		NewsfeedRequest request = new NewsfeedRequest();
		ReflectionTestUtils.setField(request, "title", "testTitle");
		ReflectionTestUtils.setField(request, "content", "testContent");
		ReflectionTestUtils.setField(request, "imageUrls", List.of("img1.jpg"));
		ReflectionTestUtils.setField(request, "mainIndex", 0);

		when(userService.findByIdOrElseThrow(any())).thenReturn(TEST_USER);
		when(newsfeedRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
		when(imageService.createImages(any(Newsfeed.class), anyList(), anyInt()))
			.thenReturn(ImageDto.of(List.of("img1.jpg"), 0));

		// when
		NewsfeedResponse response = newsfeedService.createNewsfeed(1L, request);

		// then
		verify(newsfeedRepository).save(newsfeedCaptor.capture());
		Newsfeed savedNewsfeed = newsfeedCaptor.getValue();

		assertEquals("testTitle", savedNewsfeed.getTitle());
		assertEquals("testContent", savedNewsfeed.getContent());
		assertEquals("testTitle", response.getTitle());
		assertEquals("testContent", response.getContent());
		assertEquals(List.of("img1.jpg"), response.getImageUrls());
		assertEquals(0, response.getMainIndex());
	}

	@Test
	void 새소식_단건_조회_성공() {
		// given
		Newsfeed newsfeed = Newsfeed.of(TEST_USER, "testTitle", "testContent");
		ReflectionTestUtils.setField(newsfeed, "id", 1L);

		when(newsfeedRepository.findById(1L)).thenReturn(Optional.of(newsfeed));
		when(imageService.getNewsfeedImages(1L)).thenReturn(TEST_IMAGEDTO);

		// when
		NewsfeedResponse response = newsfeedService.getNewsfeed(1L);

		// then
		assertEquals(1L, response.getId());
		assertEquals("testTitle", response.getTitle());
		assertEquals("testContent", response.getContent());
		assertEquals(TEST_IMAGEDTO.getImageUrls(), response.getImageUrls());
		assertEquals(TEST_IMAGEDTO.getMainIndex(), response.getMainIndex());

		verify(newsfeedRepository).findById(1L);
		verify(imageService).getNewsfeedImages(1L);
	}

	@Test
	void 새소식_전체_조회() {
		// given
		Newsfeed newsfeed1 = Newsfeed.of(TEST_USER, "testTitle1", "testContent1");
		ReflectionTestUtils.setField(newsfeed1, "id", 1L);

		Newsfeed newsfeed2 = Newsfeed.of(TEST_USER, "testTitle2", "testContent2");
		ReflectionTestUtils.setField(newsfeed2, "id", 2L);

		Pageable pageable = PageRequest.of(0, 10);
		Page<Newsfeed> newsfeedPage = new PageImpl<>(List.of(newsfeed1, newsfeed2), pageable, 2);
		when(newsfeedRepository.findByIsDeletedFalseWithFetchJoin(pageable)).thenReturn(newsfeedPage);

		Map<Long, Image> imageMap = Map.of(
			1L, Image.of(newsfeed1, "img1.jpg", true),
			2L, Image.of(newsfeed2, "img2.jpg", true)
		);
		when(imageService.getMainImagesForNewsfeeds(any())).thenReturn(imageMap);

		// when
		PageResponseDto<NewsfeedListResponse> response = newsfeedService.getAllNewsfeeds(pageable);

		// then
		assertEquals(2, response.getContent().size());
		assertEquals("testTitle1", response.getContent().get(0).getTitle());
		assertEquals("testTitle2", response.getContent().get(1).getTitle());

		verify(newsfeedRepository).findByIsDeletedFalseWithFetchJoin(pageable);
	}

	@Test
	void 새소식_수정_성공() {
		// given
		ReflectionTestUtils.setField(TEST_USER, "id", 1L);
		Newsfeed newsfeed = Newsfeed.of(TEST_USER, "testTitle", "testContent");

		NewsfeedRequest request = new NewsfeedRequest();
		ReflectionTestUtils.setField(request, "title", "newTitle");
		ReflectionTestUtils.setField(request, "content", "newContent");
		ReflectionTestUtils.setField(request, "imageUrls", List.of("img1.jpg"));
		ReflectionTestUtils.setField(request, "mainIndex", 0);

		when(newsfeedRepository.findById(any())).thenReturn(Optional.of(newsfeed));
		when(imageService.updateImages(any(), any(), any())).thenReturn(TEST_IMAGEDTO);

		// when
		NewsfeedResponse response = newsfeedService.updateNewsfeed(1L, request, 1L);

		// then
		assertEquals("newTitle", newsfeed.getTitle());
		assertEquals("newContent", newsfeed.getContent());
		assertEquals("newTitle", response.getTitle());
		assertEquals("newContent", response.getContent());

		verify(imageService).updateImages(any(), any(), any());
	}

	@Test
	void 새소식_삭제_성공() {
		// given
		ReflectionTestUtils.setField(TEST_USER, "id", 1L);
		Newsfeed newsfeed = Newsfeed.of(TEST_USER, "testTitle", "testContent");
		ReflectionTestUtils.setField(newsfeed, "id", 1L);

		when(newsfeedRepository.findById(any())).thenReturn(Optional.of(newsfeed));

		// when
		newsfeedService.softDeleteNewsfeed(1L, TEST_USER.getId());

		// then
		assertTrue(newsfeed.isDeleted());
	}
}
