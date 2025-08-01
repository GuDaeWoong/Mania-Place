package com.example.place.domain.item.service;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;
import com.example.place.domain.item.entity.Item;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockService {
	@Lazy
	private final ItemService itemService;
	private final RedissonClient redissonClient;

	// 재고 감소 로직
	public void decreaseStock(Long itemId, Long quantity) {
		Item item = itemService.findByIdOrElseThrow(itemId);
		if (!item.isLimitedEdition()) {
			decreaseStockWithDistributedLock(itemId, quantity);
		} else {
			decreaseStockWithRedisPessimisticLock(itemId, quantity);
		}
	}

	// 재고 증가 로직
	public void increaseStock(Long itemId, Long quantity) {
		Item item = itemService.findByIdOrElseThrow(itemId);

		if (item.getCount() == 1L && !item.isLimitedEdition()) {
			increaseStockWithDistributedLock(itemId, quantity);
		} else {
			increaseStockWithRedisPessimisticLock(itemId, quantity);
		}
	}

	// 분산락 재고감소
	private void decreaseStockWithDistributedLock(Long itemId, Long quantity) {
		String lockKey = "DistributedLock:" + itemId;
		RLock lock = redissonClient.getLock(lockKey);

		try {
			// 락을 획득하기 위해 기다릴 최대 시간 3초
			// 락을 획득했을 때 락이 자동으로 해제되기까지 걸리는 시간 10초
			boolean acquired = lock.tryLock(3000, 10000, TimeUnit.MILLISECONDS);
			// 락 획득 실패시 예외 발생
			if (!acquired) {
				throw new CustomException(ExceptionCode.STOCK_LOCK_FAILED);
			}

			Item item = itemService.findByIdOrElseThrow(itemId);

			if (item.getCount() < quantity) {
				throw new CustomException(ExceptionCode.OUT_OF_STOCK);
			}
			// 실제 제고감소
			item.decreaseStock(quantity);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new CustomException(ExceptionCode.OPERATION_INTERRUPTED);
		} finally {
			if (lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
	}

	// 분산락 재고증가
	private void increaseStockWithDistributedLock(Long itemId, Long quantity) {
		String lockKey = "DistributedLock:" + itemId;
		RLock lock = redissonClient.getLock(lockKey);
		try {
			boolean acquired = lock.tryLock(3000, 10000, TimeUnit.MILLISECONDS);
			if (!acquired) {
				throw new CustomException(ExceptionCode.STOCK_LOCK_FAILED);
			}

			Item item = itemService.findByIdOrElseThrow(itemId);
			// 재고 증가
			item.increaseStock(quantity);

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new CustomException(ExceptionCode.OPERATION_INTERRUPTED);
		} finally {
			if (lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
	}

	// Redis 비관적 락
	private void decreaseStockWithRedisPessimisticLock(Long itemId, Long quantity) {
		String lockKey = "PessimisticLock:" + itemId;
		RLock lock = redissonClient.getLock(lockKey);
		try {
			boolean acquired = lock.tryLock(3000, 10000, TimeUnit.MILLISECONDS);

			if (!acquired) {
				throw new CustomException(ExceptionCode.STOCK_LOCK_FAILED);
			}

			Item item = itemService.findByIdOrElseThrow(itemId);

			if (item.getCount() < quantity) {
				throw new CustomException(ExceptionCode.OUT_OF_STOCK);
			}

			item.decreaseStock(quantity);

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new CustomException(ExceptionCode.OPERATION_INTERRUPTED);
		} finally {
			if (lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
	}

	private void increaseStockWithRedisPessimisticLock(Long itemId, Long quantity) {
		String lockKey = "PessimisticLock:" + itemId;
		RLock lock = redissonClient.getLock(lockKey);
		try {
			boolean acquired = lock.tryLock(3000, 10000, TimeUnit.MILLISECONDS);
			if (!acquired) {
				throw new CustomException(ExceptionCode.STOCK_LOCK_FAILED);
			}

			Item item = itemService.findByIdOrElseThrow(itemId);

			item.increaseStock(quantity);

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new CustomException(ExceptionCode.OPERATION_INTERRUPTED);
		} finally {
			if (lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
	}
}