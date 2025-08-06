package com.example.place.domain.item.service;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;
import com.example.place.domain.item.entity.Item;
import com.example.place.domain.item.repository.ItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockService {
	@Lazy
	private final RedissonClient redissonClient;
	private final ItemRepository itemRepository;

	// 분산락 재고감소
	// @Transactional
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void decreaseStock(Long itemId, Long quantity) {
		String lockKey = "Lock:" + itemId;
		RLock lock = redissonClient.getLock(lockKey);

		try {
			// 락을 획득하기 위해 기다릴 최대 시간 3초
			// 락을 획득했을 때 락이 자동으로 해제되기까지 걸리는 시간 10초
			boolean acquired = lock.tryLock(3000, 10000, TimeUnit.MILLISECONDS);
			// 락 획득 실패시 예외 발생
			if (!acquired) {
				throw new CustomException(ExceptionCode.STOCK_LOCK_FAILED);
			}

			try {
				Item item = itemRepository.findByIdWithLock(itemId)
					.orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_ITEM));

				// 실제 제고감소
				item.decreaseStock(quantity);

			}finally {
				if (lock.isHeldByCurrentThread()) {
					lock.unlock();
				}
			}

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new CustomException(ExceptionCode.OPERATION_INTERRUPTED);
		}
	}

	// 분산락 재고증가
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void increaseStock(Long itemId, Long quantity) {
		String lockKey = "Lock:" + itemId;
		RLock lock = redissonClient.getLock(lockKey);
		try {
			boolean acquired = lock.tryLock(3000, 10000, TimeUnit.MILLISECONDS);
			if (!acquired) {
				throw new CustomException(ExceptionCode.STOCK_LOCK_FAILED);
			}

			try {
				Item item = itemRepository.findByIdWithLock(itemId)
					.orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_ITEM));

				item.increaseStock(quantity);

			} finally {
				if (lock.isHeldByCurrentThread()) {
					lock.unlock();
				}
			}

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new CustomException(ExceptionCode.OPERATION_INTERRUPTED);
		}
	}
}