package com.example.place.domain.order.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.place.domain.order.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

	@Query(value = """
	select o from Order o
		join fetch o.item i
			join fetch o.user u
				where o.user.id = :userId
					""",
		countQuery = """
	select count(o) from Order o
	where o.user.id = :userId
	""")
	Page<Order> findAllByUserIdWithItemAndUser(Long userId, Pageable pageable);

	List<Order> findByItemId(Long itemId);
}
