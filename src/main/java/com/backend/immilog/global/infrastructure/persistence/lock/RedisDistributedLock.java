package com.backend.immilog.global.infrastructure.persistence.lock;

import com.backend.immilog.global.infrastructure.persistence.repository.DataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RedisDistributedLock {
    final int MAX_RETRY = 3;
    final int EXPIRE_TIME_IN_SECONDS = 10;
    final long RETRY_DELAY_MILLIS = 500;
    private final DataRepository dataRepository;
    public RedisDistributedLock(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public boolean tryAcquireLock(
            String key,
            String targetSeq
    ) {
        boolean lockAcquired = false;
        int retryCount = 0;
        while (!lockAcquired && retryCount < MAX_RETRY) {
            lockAcquired = acquireLock(
                    key,
                    targetSeq,
                    EXPIRE_TIME_IN_SECONDS
            );
            retryCount = getRetryCount(lockAcquired, retryCount);
        }
        return lockAcquired;
    }

    public void releaseLock(
            String lockKey,
            String requestId
    ) {
        try {
            String storedRequestId = dataRepository.findByKey(lockKey);
            if (requestId.equals(storedRequestId)) {
                dataRepository.deleteByKey(lockKey);
            }
        } catch (DataAccessException e) {
            log.error("Failed to release lock", e);
        }
    }

    private boolean acquireLock(
            String lockKey,
            String requestId,
            long expireTimeInSeconds
    ) {
        try {
            // DataRepository를 사용하여 setIfAbsent 처리
            Boolean lockAcquired = dataRepository.saveIfAbsent(
                    lockKey,
                    requestId,
                    expireTimeInSeconds
            );
            return lockAcquired != null && lockAcquired;
        } catch (DataAccessException e) {
            log.error("Failed to acquire lock", e);
            return false;
        }
    }

    public int getRetryCount(
            boolean lockAcquired,
            int retryCount
    ) {
        if (!lockAcquired) {
            try {
                Thread.sleep(RETRY_DELAY_MILLIS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            retryCount++;
        }
        return retryCount;
    }
}

