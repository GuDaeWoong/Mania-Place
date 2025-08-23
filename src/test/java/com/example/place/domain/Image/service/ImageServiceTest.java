package com.example.place.domain.Image.service;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
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
import org.springframework.test.util.ReflectionTestUtils;

import com.example.place.domain.Image.dto.ImageDto;
import com.example.place.domain.Image.entity.Image;
import com.example.place.domain.Image.repository.ImageRepository;
import com.example.place.domain.item.entity.Item;
import com.example.place.domain.newsfeed.entity.Newsfeed;
import com.example.place.domain.post.entity.Post;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.user.entity.UserRole;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

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
	private static final Newsfeed TEST_NEWSFEED = Newsfeed.of(
		TEST_USER,
		"testTitle",
		"testContent"
	);

	@Mock
	private ImageRepository imageRepository;

	@InjectMocks
	private ImageService imageService;

	@Captor
	ArgumentCaptor<Image> imageCaptor;

	@Test
	void 상품_이미지_저장_성공() {
		// when
		imageService.createImages(TEST_ITEM, List.of("img1.jpg", "img2.jpg"), 0);

		// then
		verify(imageRepository, times(2)).save(imageCaptor.capture());
		List<Image> savedImages = imageCaptor.getAllValues();

		// -- file1에 관한 검증
		assertEquals(TEST_ITEM, savedImages.get(0).getItem());
		assertEquals("img1.jpg", savedImages.get(0).getImageUrl());
		assertTrue(savedImages.get(0).isMain());

		// -- file2에 관한 검증
		assertEquals(TEST_ITEM, savedImages.get(1).getItem());
		assertEquals("img2.jpg", savedImages.get(1).getImageUrl());
		assertFalse(savedImages.get(1).isMain());
	}

	@Test
	void 새소식_이미지_저장_성공() {
		// when
		imageService.createImages(TEST_NEWSFEED, List.of("img1.jpg", "img2.jpg"), 0);

		// then
		verify(imageRepository, times(2)).save(imageCaptor.capture());
		List<Image> savedImages = imageCaptor.getAllValues();

		// -- file1에 관한 검증
		assertEquals(TEST_NEWSFEED, savedImages.get(0).getNewsfeed());
		assertEquals("img1.jpg", savedImages.get(0).getImageUrl());
		assertTrue(savedImages.get(0).isMain());

		// -- file2에 관한 검증
		assertEquals(TEST_NEWSFEED, savedImages.get(1).getNewsfeed());
		assertEquals("img2.jpg", savedImages.get(1).getImageUrl());
		assertFalse(savedImages.get(1).isMain());
	}

	@Test
	void 상품_이미지_조회_성공() {
		// given
		List<Image> existingimages = List.of(
			Image.of(TEST_ITEM, "img1.jpg", true),
			Image.of(TEST_ITEM, "img2.jpg", false));
		when(imageRepository.findByItemId(1L)).thenReturn(existingimages);

		// when
		ImageDto dto = imageService.getItemImages(1L);

		// then
		assertEquals(List.of("img1.jpg", "img2.jpg"), dto.getImageUrls());
		assertEquals(0, dto.getMainIndex());
	}

	@Test
	void 새소식_이미지_조회_성공() {
		// given
		List<Image> existingimages = List.of(
			Image.of(TEST_NEWSFEED, "img1.jpg", true),
			Image.of(TEST_NEWSFEED, "img2.jpg", false));
		when(imageRepository.findByNewsfeedId(1L)).thenReturn(existingimages);

		// when
		ImageDto dto = imageService.getNewsfeedImages(1L);

		// then
		assertEquals(List.of("img1.jpg", "img2.jpg"), dto.getImageUrls());
		assertEquals(0, dto.getMainIndex());
	}

	// 상품 이미지 수정
	@Test
	void 상품_이미지_수정_이미지삭제_성공() {
		// given
		List<Image> existingimages = List.of(
			Image.of(TEST_ITEM, "img1.jpg", true),
			Image.of(TEST_ITEM, "img2.jpg", false));

		List<Image> newimages = List.of(Image.of(TEST_ITEM, "img1.jpg", true));

		when(imageRepository.findByItemId(TEST_ITEM.getId()))
			.thenReturn(existingimages) // 수정 전 조회
			.thenReturn(newimages);     // 수정 후 재조회

		// when
		imageService.updateImages(TEST_ITEM, List.of("img1.jpg"), 0);

		// then
		verify(imageRepository).delete(argThat(img -> img.getImageUrl().equals("img2.jpg")));
		verify(imageRepository, never()).save(any());
	}

	@Test
	void 상품_이미지_수정_이미지저장_성공() {
		// given
		List<Image> existingimages = List.of(Image.of(TEST_ITEM, "img1.jpg", true));

		List<Image> newimages = List.of(
			Image.of(TEST_ITEM, "img1.jpg", true),
			Image.of(TEST_ITEM, "img2.jpg", false));

		when(imageRepository.findByItemId(TEST_ITEM.getId()))
			.thenReturn(existingimages) // 수정 전 조회
			.thenReturn(newimages);     // 수정 후 재조회

		// when
		imageService.updateImages(TEST_ITEM, List.of("img1.jpg", "img2.jpg"), 0);

		// then
		verify(imageRepository).save(argThat(img -> img.getImageUrl().equals("img2.jpg")));
		verify(imageRepository, never()).delete(any());
	}

	@Test
	void 상품_이미지_수정_대표이미지변경_성공() {
		Image image1 = spy(Image.of(TEST_ITEM, "img1.jpg", true));
		Image image2 = spy(Image.of(TEST_ITEM, "img2.jpg", false));

		when(imageRepository.findByItemId(TEST_ITEM.getId()))
			.thenReturn(List.of(image1, image2))  // 수정 전 조회
			.thenReturn(List.of(image1, image2)); // 수정 후 재조회 (이미지 삭제 및 추가 저장 후, 대표 이미지 변경 전 재조회 발생)

		// when
		imageService.updateImages(TEST_ITEM, List.of("img1.jpg", "img2.jpg"), 1);

		// then
		verify(image1).updateIsMain(false);
		verify(image2).updateIsMain(true);
	}

	// 새소식 이미지 수정
	@Test
	void 새소식_이미지_수정_이미지삭제_성공() {
		// given
		List<Image> existingimages = List.of(
			Image.of(TEST_NEWSFEED, "img1.jpg", true),
			Image.of(TEST_NEWSFEED, "img2.jpg", false));

		List<Image> newimages = List.of(Image.of(TEST_NEWSFEED, "img1.jpg", true));

		when(imageRepository.findByNewsfeedId(TEST_NEWSFEED.getId()))
			.thenReturn(existingimages) // 수정 전 조회
			.thenReturn(newimages);     // 수정 후 재조회

		// when
		imageService.updateImages(TEST_NEWSFEED, List.of("img1.jpg"), 0);

		// then
		verify(imageRepository).delete(argThat(img -> img.getImageUrl().equals("img2.jpg")));
		verify(imageRepository, never()).save(any());
	}

	@Test
	void 새소식_이미지_수정_이미지저장_성공() {
		// given
		List<Image> existingimages = List.of(Image.of(TEST_NEWSFEED, "img1.jpg", true));

		List<Image> newimages = List.of(
			Image.of(TEST_NEWSFEED, "img1.jpg", true),
			Image.of(TEST_NEWSFEED, "img2.jpg", false));

		when(imageRepository.findByNewsfeedId(TEST_NEWSFEED.getId()))
			.thenReturn(existingimages) // 수정 전 조회
			.thenReturn(newimages);     // 수정 후 재조회

		// when
		imageService.updateImages(TEST_NEWSFEED, List.of("img1.jpg", "img2.jpg"), 0);

		// then
		verify(imageRepository).save(argThat(img -> img.getImageUrl().equals("img2.jpg")));
		verify(imageRepository, never()).delete(any());
	}

	@Test
	void 새소식_이미지_수정_대표이미지변경_성공() {
		Image image1 = spy(Image.of(TEST_NEWSFEED, "img1.jpg", true));
		Image image2 = spy(Image.of(TEST_NEWSFEED, "img2.jpg", false));

		when(imageRepository.findByNewsfeedId(TEST_NEWSFEED.getId()))
			.thenReturn(List.of(image1, image2))  // 수정 전 조회
			.thenReturn(List.of(image1, image2)); // 수정 후 재조회 (이미지 삭제 및 추가 저장 후, 대표 이미지 변경 전 재조회 발생)

		// when
		imageService.updateImages(TEST_NEWSFEED, List.of("img1.jpg", "img2.jpg"), 1);

		// then
		verify(image1).updateIsMain(false);
		verify(image2).updateIsMain(true);
	}

	@Test
	void 상품_이미지_삭제_성공() {
		// given
		List<Image> images = List.of(Image.of(TEST_ITEM, "img1.jpg", true));
		when(imageRepository.findByItemId(TEST_ITEM.getId())).thenReturn(images);

		// when
		imageService.deleteImageByItemId(TEST_ITEM.getId());

		// then
		verify(imageRepository).deleteAll(images);
	}

	@Test
	void 새소식_이미지_삭제_성공() {
		// given
		List<Image> images = List.of(Image.of(TEST_NEWSFEED, "img1.jpg", true));
		when(imageRepository.findByNewsfeedId(TEST_NEWSFEED.getId())).thenReturn(images);

		// when
		imageService.deleteImageByNewsfeedId(TEST_NEWSFEED.getId());

		// then
		verify(imageRepository).deleteAll(images);
	}

	@Test
	void 이미지_저장_대표인덱스_음수_기본값적용_성공() {
		// when
		imageService.createImages(TEST_ITEM, List.of("img1.jpg", "img2.jpg"), -1);

		// then
		verify(imageRepository, times(2)).save(imageCaptor.capture());
		List<Image> savedImages = imageCaptor.getAllValues();

		assertTrue(savedImages.get(0).isMain());
		assertFalse(savedImages.get(1).isMain());
	}

	@Test
	void 이미지_저장_대표인덱스_범위초과_기본값적용_성공() {
		// when
		imageService.createImages(TEST_ITEM, List.of("img1.jpg", "img2.jpg"), 2);

		// then
		verify(imageRepository, times(2)).save(imageCaptor.capture());
		List<Image> savedImages = imageCaptor.getAllValues();

		assertTrue(savedImages.get(0).isMain());
		assertFalse(savedImages.get(1).isMain());
	}

	@Test
	void 상품_대표이미지_조회_성공() {
		// given
		List<Image> images = List.of(
			Image.of(TEST_NEWSFEED, "img1.jpg", true),
			Image.of(TEST_NEWSFEED, "img2.jpg", false));

		when(imageRepository.findByItemIdAndIsMainTrue(TEST_NEWSFEED.getId())).thenReturn(Optional.of(images.get(0)));

		// when
		String mainImageUrl = imageService.getMainImageUrl(TEST_NEWSFEED.getId());

		// then
		assertEquals("img1.jpg", mainImageUrl);
	}

	// 상품 대표이미지 맵 조회
	@Test
	void 상품_페이지_대표이미지_맵_조회_성공() {
		// given
		ReflectionTestUtils.setField(TEST_ITEM, "id", 1L);
		Page<Item> pagedItems = new PageImpl<>(List.of(TEST_ITEM));

		Image image = Image.of(TEST_ITEM, "img1.jpg", true);

		when(imageRepository.findMainImagesByItemIds(List.of(TEST_ITEM.getId())))
			.thenReturn(List.of(image));

		// when
		Map<Long, Image> result = imageService.getMainImagesForItems(pagedItems);

		// then
		assertThat(result).hasSize(1);
		assertEquals(image, result.get(TEST_ITEM.getId()));
	}

	@Test
	void 상품_빈페이지_대표이미지_맵_조회_성공() {
		// given
		Page<Item> pagedItems = new PageImpl<>(List.of());

		// when
		Map<Long, Image> result = imageService.getMainImagesForItems(pagedItems);

		// then
		assertThat(result).isEmpty();
		verify(imageRepository, never()).findMainImagesByItemIds(any());
	}

	// 게시글 대표이미지 맵
	@Test
	void 게시글_페이지_대표이미지_맵_조회_성공() {
		// given
		ReflectionTestUtils.setField(TEST_ITEM, "id", 1L);
		ReflectionTestUtils.setField(TEST_POST, "id", 1L);
		Page<Post> pagedPosts = new PageImpl<>(List.of(TEST_POST));

		Image image = Image.of(TEST_ITEM, "img1.jpg", true);

		when(imageRepository.findMainImagesByItemIds(List.of(TEST_ITEM.getId())))
			.thenReturn(List.of(image));

		// when
		Map<Long, Image> result = imageService.getMainImagesForPosts(pagedPosts);

		// then
		assertThat(result).hasSize(1);
		assertEquals(image, result.get(TEST_ITEM.getId()));
	}

	@Test
	void 게시글_빈페이지_대표이미지_맵_조회_성공() {
		// given
		Page<Post> pagedPosts = new PageImpl<>(List.of());

		// when
		Map<Long, Image> result = imageService.getMainImagesForPosts(pagedPosts);

		// then
		assertThat(result).isEmpty();
		verify(imageRepository, never()).findMainImagesByItemIds(any());
	}

	@Test
	void 새소식_페이지_대표이미지_맵_조회_성공() {
		// given
		ReflectionTestUtils.setField(TEST_NEWSFEED, "id", 1L);
		Page<Newsfeed> pagedNewsfeeds = new PageImpl<>(List.of(TEST_NEWSFEED));

		Image image = Image.of(TEST_NEWSFEED, "img1.jpg", true);

		when(imageRepository.findMainImagesByNewsfeedIds(List.of(TEST_NEWSFEED.getId())))
			.thenReturn(List.of(image));

		// when
		Map<Long, Image> result = imageService.getMainImagesForNewsfeeds(pagedNewsfeeds);

		// then
		assertThat(result).hasSize(1);
		assertEquals(image, result.get(TEST_NEWSFEED.getId()));
	}

	@Test
	void 새소식_빈페이지_대표이미지_맵_조회_성공() {
		// given
		Page<Newsfeed> pagedNesfeeds = new PageImpl<>(List.of());

		// when
		Map<Long, Image> result = imageService.getMainImagesForNewsfeeds(pagedNesfeeds);

		// then
		assertThat(result).isEmpty();
		verify(imageRepository, never()).findMainImagesByNewsfeedIds(any());
	}
}