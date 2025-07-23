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
	private static final Item TEST_ITEM = Item.of(
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
		// when
		imageService.saveImages(TEST_ITEM, List.of("file1.jpg", "file2.jpg"), 0);

		// then
		verify(imageRepository, times(2)).save(imageCaptor.capture());
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

		List<Image> newimages = List.of(Image.of(TEST_ITEM, "file1.jpg", true));

		when(imageRepository.findByItemId(TEST_ITEM.getId()))
			.thenReturn(existingimages) // 수정 전 조회
			.thenReturn(newimages);     // 수정 후 재조회

		// when
		imageService.updateImages(TEST_ITEM, List.of("file1.jpg"), 0);

		// then
		verify(imageRepository).delete(argThat(img -> img.getImageUrl().equals("file2.jpg")));
		verify(imageRepository, never()).save(any());
	}

	@Test
	void 이미지_수정_이미지저장_성공() {
		// given
		List<Image> existingimages = List.of(Image.of(TEST_ITEM, "file1.jpg", true));

		List<Image> newimages = List.of(
			Image.of(TEST_ITEM, "file1.jpg", true),
			Image.of(TEST_ITEM, "file2.jpg", false));

		when(imageRepository.findByItemId(TEST_ITEM.getId()))
			.thenReturn(existingimages) // 수정 전 조회
			.thenReturn(newimages);     // 수정 후 재조회

		// when
		imageService.updateImages(TEST_ITEM, List.of("file1.jpg", "file2.jpg"), 0);

		// then
		verify(imageRepository).save(argThat(img -> img.getImageUrl().equals("file2.jpg")));
		verify(imageRepository, never()).delete(any());
	}

	@Test
	void 이미지_수정_대표이미지변경_성공() {
		Image image1 = spy(Image.of(TEST_ITEM, "file1.jpg", true));
		Image image2 = spy(Image.of(TEST_ITEM, "file2.jpg", false));

		when(imageRepository.findByItemId(TEST_ITEM.getId()))
			.thenReturn(List.of(image1, image2))  // 수정 전 조회
			.thenReturn(List.of(image1, image2)); // 수정 후 재조회 (이미지 삭제 및 추가 저장 후, 대표 이미지 변경 전 재조회 발생)

		// when
		imageService.updateImages(TEST_ITEM, List.of("file1.jpg", "file2.jpg"), 1);

		// then
		verify(image1).updateIsMain(false);
		verify(image2).updateIsMain(true);
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

	@Test
	void 이미지_저장_대표인덱스_음수_기본값적용_성공() {
		// when
		imageService.saveImages(TEST_ITEM, List.of("file1.jpg", "file2.jpg"), -1);

		// then
		verify(imageRepository, times(2)).save(imageCaptor.capture());
		List<Image> savedImages = imageCaptor.getAllValues();

		assertTrue(savedImages.get(0).isMain());
		assertFalse(savedImages.get(1).isMain());
	}

	@Test
	void 이미지_저장_대표인덱스_범위초과_기본값적용_성공() {
		// when
		imageService.saveImages(TEST_ITEM, List.of("file1.jpg", "file2.jpg"), 2);

		// then
		verify(imageRepository, times(2)).save(imageCaptor.capture());
		List<Image> savedImages = imageCaptor.getAllValues();

		assertTrue(savedImages.get(0).isMain());
		assertFalse(savedImages.get(1).isMain());
	}
}