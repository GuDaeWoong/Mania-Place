package com.example.place.domain.newsfeed.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
import com.example.place.domain.newsfeed.entity.Newsfeed;
import com.example.place.domain.newsfeed.repository.NewsfeedRepository;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.user.service.UserService;
import com.example.place.domain.user.entity.UserRole;
import com.example.place.common.dto.PageResponseDto;
import com.example.place.domain.newsfeed.dto.response.NewsfeedListResponse;

@ExtendWith(MockitoExtension.class)
public class NewsfeedServiceTest {

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

	@InjectMocks
	private NewsfeedService newsfeedService;

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

	private static void setId(Object target, Long id) {
		ReflectionTestUtils.setField(target, "id", id);
	}

	@Captor
	ArgumentCaptor<Newsfeed> newsfeedCaptor;

	@Test
	void 새소식_등록_성공() {
		// give
		NewsfeedRequest request = new NewsfeedRequest();
		ReflectionTestUtils.setField(request, "title", "테스트 제목");
		ReflectionTestUtils.setField(request, "content", "테스트 내용");
		ReflectionTestUtils.setField(request, "imageUrls", List.of("img1.jpg", "img2.jpg"));
		ReflectionTestUtils.setField(request, "mainIndex", 0);

		when(userService.findByIdOrElseThrow(any())).thenReturn(TEST_USER);

		// when
		NewsfeedResponse response = newsfeedService.createNewsfeed(1L, request);

		// then
		verify(newsfeedRepository, times(1)).save(newsfeedCaptor.capture());
		assertEquals(request.getTitle(), newsfeedCaptor.getValue().getTitle());
		assertEquals(request.getContent(), newsfeedCaptor.getValue().getContent());
		assertEquals(request.getTitle(), response.getTitle());
		assertEquals(request.getContent(), response.getContent());
		assertEquals(request.getImageUrls(), response.getImageUrls());
		assertEquals(request.getMainIndex(), response.getMainIndex());
	}

	@Test
	void 새소식_단건_조회_성공() {
		// given
		Newsfeed newsfeed = Newsfeed.of(
			TEST_USER,
			"testTitle",
			"testContent"
		);
		setId(newsfeed, 1L);

		when(newsfeedRepository.findById(1L)).thenReturn(Optional.of(newsfeed));
		when(imageService.getNewsfeedImages(1L)).thenReturn(TEST_IMAGEDTO);

		// when
		NewsfeedResponse res = newsfeedService.getNewsfeed(1L);

		// then
		assertEquals(1L, res.getId());
		assertEquals("testTitle", res.getTitle());
		assertEquals("testContent", res.getContent());
		assertEquals(TEST_IMAGEDTO.getImageUrls(), res.getImageUrls());
		assertEquals(TEST_IMAGEDTO.getMainIndex(), res.getMainIndex());

		verify(newsfeedRepository).findById(1L);
		verify(imageService).getNewsfeedImages(1L);
	}

	@Test
	void 새소식_전체_조회() {
		Newsfeed newsfeed1 = Newsfeed.of(
			TEST_USER,
			"testTitle1",
			"testContent1"
		);
		setId(newsfeed1, 1L);

		Newsfeed newsfeed2 = Newsfeed.of(
			TEST_USER,
			"testTitle2",
			"testContent2"
		);
		setId(newsfeed2, 2L);

		Pageable pageable = PageRequest.of(0, 10);
		Page<Newsfeed> newsfeedPage = new PageImpl<>(List.of(newsfeed1, newsfeed2), pageable, 2);

		when(newsfeedRepository.findByIsDeletedFalseWithFetchJoin(pageable)).thenReturn(newsfeedPage);

		Map<Long, Image> imageMap = Map.of(
			1L, Image.of(newsfeed1, "img1.jpg", true),
			2L, Image.of(newsfeed2, "img2.jpg", true)
		);
		when(imageService.getMainImagesForNewsfeeds(any())).thenReturn(imageMap);

		// when
		PageResponseDto<NewsfeedListResponse> result = newsfeedService.getAllNewsfeeds(pageable);

		// then
		assertEquals(2, result.getContent().size());
		assertEquals("testTitle1", result.getContent().get(0).getTitle());
		assertEquals("testTitle2", result.getContent().get(1).getTitle());

		verify(newsfeedRepository).findByIsDeletedFalseWithFetchJoin(pageable);
	}

	@Test
	void 새소식_수정_성공() {
		// given
		setId(TEST_USER, 1L);
		Newsfeed newsfeed = Newsfeed.of(
			TEST_USER,
			"testTitle",
			"testContent"
		);

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

		verify(imageService, times(1)).updateImages(any(), any(), any());
	}

	@Test
	void 새소식_삭제_성공() {
		// given
		setId(TEST_USER, 1L);
		Newsfeed newsfeed = Newsfeed.of(
			TEST_USER,
			"testTitle",
			"testContent");

		when(newsfeedRepository.findById(any())).thenReturn(Optional.of(newsfeed));

		// when
		newsfeedService.softDeleteNewsfeed(1L, TEST_USER.getId());

		// then
		assertTrue(newsfeed.isDeleted());
	}

}