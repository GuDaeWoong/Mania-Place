package com.example.place.domain.item.repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.example.place.domain.Image.entity.QImage;
import com.example.place.domain.item.entity.Item;
import com.example.place.domain.item.entity.QItem;
import com.example.place.domain.itemtag.entity.QItemTag;
import com.example.place.domain.tag.entity.QTag;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.usertag.entity.QUserTag;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<Item> findByUserTag(User user, Pageable pageable) {
		QItem item = QItem.item;
		QItemTag itemTag = QItemTag.itemTag;
		QUserTag userTag = QUserTag.userTag;
		QTag tag = QTag.tag;

		// 페이지의 id 조회
		JPQLQuery<Long> interestedTagIds = JPAExpressions
			.select(userTag.tag.id)
			.from(userTag)
			.where(userTag.user.eq(user));

		List<Long> itemIds = queryFactory
			.select(item.id)
			.from(item)
			.join(item.itemTags, itemTag)
			.join(itemTag.tag, tag)
			.where(
				item.isDeleted.isFalse(),
				tag.id.in(interestedTagIds))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		if (itemIds.isEmpty()) {
			return new PageImpl<>(Collections.emptyList(), pageable, 0);
		}

		// 페이지의 상품들 정보 조회
		List<Item> items = queryFactory
			.selectFrom(item)
			.distinct()
			.join(item.itemTags, itemTag).fetchJoin()
			.join(itemTag.tag, tag).fetchJoin()
			.join(item.images).fetchJoin()
			.where(item.id.in(itemIds))
			.fetch();

		// 페이징 처리를 위한 전체 열 카운트
		long total = Optional.ofNullable(
			queryFactory
				.select(item.countDistinct()) // fetchJoin 없이 count 하기위해 distinct
				.from(item)
				.join(item.itemTags, itemTag)
				.join(itemTag.tag, tag)
				.where(item.isDeleted.isFalse())
				.fetchOne()
		).orElse(0L);

		return new PageImpl<>(items, pageable, total);
	}
}
