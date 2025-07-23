package com.example.place.domain.Image.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.place.domain.Image.entity.Image;
import com.example.place.domain.Image.repository.ImageRepository;
import com.example.place.domain.item.entity.Item;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.user.entity.UserRole;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

	private static final User TEST_USER = User.of(
		"testUser",
		"testNickname",
		"test@email.com",
		"Test1234!",
		null,
		UserRole.USER
	);
	private static final Item TEST_ITEM = new Item(
		TEST_USER,
		"testItem",
		"testDescription",
		100.0,
		1L,
		LocalDateTime.parse("2025-07-23T00:00:00"),
		LocalDateTime.parse("2125-07-23T00:00:00")
	);

	@Mock
	private ImageRepository imageRepository;

	@InjectMocks
	private ImageService imageService;

	@Captor
	ArgumentCaptor<Image> imageCaptor;

	@Test
	void 이미지_저장_성공() {
		// given
		List<String> imageUrls = Arrays.asList("file1.jpg", "file2.jpg");

		// when
		imageService.saveImages(TEST_ITEM, imageUrls, 0);

		// then
		verify(imageRepository, times(imageUrls.size())).save(imageCaptor.capture());
		List<Image> savedImages = imageCaptor.getAllValues();

		// -- file1에 관한 검증
		assertEquals(TEST_ITEM, savedImages.get(0).getItem());
		assertEquals("file1.jpg", savedImages.get(0).getImageUrl());
		assertTrue(savedImages.get(0).isMain());

		// -- file2에 관한 검증
		assertEquals(TEST_ITEM, savedImages.get(1).getItem());
		assertEquals("file2.jpg", savedImages.get(1).getImageUrl());
		assertFalse(savedImages.get(1).isMain());
	}

	@Test
	void 이미지_수정_이미지삭제_성공() {
		// given
		List<Image> existingimages = List.of(
			Image.of(TEST_ITEM, "file1.jpg", true),
			Image.of(TEST_ITEM, "file2.jpg", false));

		List<String> newImageUrls = List.of("file1.jpg");

		List<Image> newimages = List.of(Image.of(TEST_ITEM, "file1.jpg", true));

		when(imageRepository.findByItemId(TEST_ITEM.getId()))
			.thenReturn(existingimages) // 수정 전 조회
			.thenReturn(newimages);     // 수정 후 재조회

		// when
		imageService.updateImages(TEST_ITEM, newImageUrls, 0);

		// then
		verify(imageRepository).delete(argThat(img -> img.getImageUrl().equals("file2.jpg")));
		verify(imageRepository, never()).save(any());
	}

	@Test
	void 이미지_삭제_성공() {
		// given
		List<Image> images = List.of(Image.of(TEST_ITEM, "file1.jpg", true));
		when(imageRepository.findByItemId(TEST_ITEM.getId())).thenReturn(images);

		// when
		imageService.deleteImageByItemId(TEST_ITEM.getId());

		// then
		verify(imageRepository).deleteAll(images);
	}
}