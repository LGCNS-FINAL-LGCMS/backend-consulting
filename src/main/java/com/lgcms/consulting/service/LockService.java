package com.lgcms.consulting.service;

import com.lgcms.consulting.common.dto.exception.BaseException;
import com.lgcms.consulting.common.dto.exception.LockError;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class LockService {
    private final RedissonClient redissonClient;

    public <T> T executeWithLock(String lockName, Supplier<T> supplier, long waitTime, long leaseTime) {
        RLock lock = redissonClient.getLock(lockName);
        try {
            boolean isLocked = lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);

            if (!isLocked) {
                throw new BaseException(LockError.LOCK_ALREADY_HELD);
            }

            return supplier.get();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BaseException(LockError.LOCK_INTERRUPTED);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    public void executeWithLock(String lockName, Runnable runnable , long waitTime, long leaseTime) {
        RLock lock = redissonClient.getLock(lockName);
        try {
            boolean isLocked = lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);

            if(!isLocked) {
                throw new BaseException(LockError.LOCK_ALREADY_HELD);
            }

            runnable.run();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BaseException(LockError.LOCK_INTERRUPTED);
        } finally {
            if (lock.isHeldByCurrentThread())
                lock.unlock();
        }
    }
}
