package com.example.place.domain.item.repository;

import static com.example.place.domain.item.entity.QItem.*;
import static com.example.place.domain.itemtag.entity.QItemTag.*;
import static com.example.place.domain.tag.entity.QTag.*;
import static com.example.place.domain.user.entity.QUser.*;
import static com.example.place.domain.usertag.entity.QUserTag.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.place.domain.item.entity.Item;
import com.example.place.domain.user.entity.User;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<Item> findAllCustom(Pageable pageable) {
		BooleanExpression whereCondition = null;
		return buildPagedItem(whereCondition, pageable);
	}

	@Override
	public Page<Item> findByUserTag(User user, Pageable pageable) {

		// 유저의 관심 태그를 조회하는 서브쿼리
		JPQLQuery<Long> interestedTagIds = JPAExpressions
			.select(userTag.tag.id)
			.from(userTag)
			.where(userTag.user.eq(user));
		// 유저의 관심 태그가 있는 상품 조회를 위한 where절
		BooleanExpression whereCondition = tag.id.in(interestedTagIds);

		return buildPagedItem(whereCondition, pageable);
	}

	@Override
	public Page<Item> search(String keyword, List<String> tags, Long userId, Pageable pageable) {

		// 상품 조회를 위한 where절
		BooleanExpression whereCondition = null;

		// 키워드를 전달 받았다면 해당 키워드가 제목or설명에 포함된 상품을 조회
		if (keyword != null) {
			BooleanExpression keywordCondition = item.itemName.likeIgnoreCase("%" + keyword + "%")
				.or(item.itemDescription.likeIgnoreCase("%" + keyword + "%"));
			whereCondition = whereCondition == null ? keywordCondition : whereCondition.and(keywordCondition);
		}

		// 태그를 전달 받았다면 해당 태그가 존재하는 상품을 조회
		if (tags != null && !tags.isEmpty()) {
			BooleanExpression tagCondition = tag.tagName.in(tags);
			whereCondition = whereCondition == null ? tagCondition : whereCondition.and(tagCondition);
		}

		// 유저 id를 전달 받았다면 해당 판매자가 해당 id와 일치하는 상품을 조회
		if (userId != null) {
			BooleanExpression userCondition = item.user.id.eq(userId);
			whereCondition = whereCondition == null ? userCondition : whereCondition.and(userCondition);
		}

		return buildPagedItem(whereCondition, pageable);
	}

	private Page<Item> buildPagedItem(BooleanExpression whereCondition, Pageable pageable) {

		// 정렬 조건
		OrderSpecifier<?> orderSpecifier = getOrderSpecifiers(pageable);

		// 페이지의 id 조회
		List<Long> itemIds = queryFactory
			.select(item.id)
			.from(item)
			.join(item.itemTags, itemTag)
			.join(itemTag.tag, tag)
			.where(
				item.isDeleted.isFalse(),
				whereCondition)
			.orderBy(orderSpecifier)
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
			.join(item.user).fetchJoin()
			.join(item.itemTags, itemTag).fetchJoin()
			.join(itemTag.tag, tag).fetchJoin()
			.where(item.id.in(itemIds))
			.orderBy(orderSpecifier)
			.fetch();

		// 페이징 처리를 위한 전체 열 카운트
		long total = Optional.ofNullable(
			queryFactory
				.select(item.countDistinct()) // fetchJoin 없이 count 하기위해 distinct
				.from(item)
				.join(item.itemTags, itemTag)
				.join(itemTag.tag, tag)
				.where(
					item.isDeleted.isFalse(),
					whereCondition)
				.fetchOne()
		).orElse(0L);

		return new PageImpl<>(items, pageable, total);
	}

	private OrderSpecifier<?> getOrderSpecifiers(Pageable pageable) {
		// Query DSL 필드 접근 경로(path) 지정 도구
		PathBuilder<Item> path = new PathBuilder<>(Item.class, "item");

		// 필터링해서 createdAt 정렬만 받고, 없으면 기본값(최신순) 세팅
		Sort.Order firstOrder = pageable.getSort().stream()
			.filter(order -> "createdAt".equals(order.getProperty()))
			.findFirst()
			.orElse(new Sort.Order(Sort.Direction.DESC, "createdAt"));

		// pageable의 정렬 값을 Query DSL에 적용가능한 형탤 변환 ("createdAt,desc" -> item.createdAt.desc())
		return new OrderSpecifier<>(
			firstOrder.isAscending() ? Order.ASC : Order.DESC,               // .asc()/.desc() 판별
			path.getComparable(firstOrder.getProperty(), Comparable.class)); // "createdAt" -> item.createdAt
	}
}
