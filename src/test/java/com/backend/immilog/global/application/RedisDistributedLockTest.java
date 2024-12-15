package com.backend.immilog.global.application;
import com.backend.immilog.global.infrastructure.persistence.lock.RedisDistributedLock;
import com.backend.immilog.global.infrastructure.persistence.repository.DataRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("RedisDistributedLock 테스트")
class RedisDistributedLockTest {
    private final DataRepository dataRepository = mock(DataRepository.class);
    private final RedisDistributedLock redisDistributedLock = new RedisDistributedLock(dataRepository);

    @Test
    @DisplayName("락 획득 시도 (최대 3회)")
    void acquireLock_FailsTwiceThenSucceeds() {
        // given
        String lockKey = "lockKey";
        String requestId = "requestId";

        // 첫 번째와 두 번째 시도는 실패 (false 반환), 세 번째 시도는 성공 (true 반환)
        when(dataRepository.saveIfAbsent(lockKey, requestId, 10))
                .thenReturn(false)
                .thenReturn(false)
                .thenReturn(true);

        // when
        boolean result = redisDistributedLock.tryAcquireLock(lockKey, requestId);

        // then
        assertThat(result).isTrue(); // 락이 성공적으로 획득되었는지 검증

        // saveIfAbsent가 3번 호출되었는지 확인
        verify(dataRepository, times(3)).saveIfAbsent(lockKey, requestId, 10);
    }

    @Test
    @DisplayName("락 해제 성공")
    void releaseLock_WhenRequestIdMatches_ShouldDeleteLock() {
        // given
        String lockKey = "lockKey";
        String requestId = "requestId";

        // dataRepository에서 반환하는 값이 requestId와 일치할 때
        when(dataRepository.findByKey(lockKey)).thenReturn(requestId);

        // when
        redisDistributedLock.releaseLock(lockKey, requestId);

        // then
        verify(dataRepository, times(1)).deleteByKey(lockKey); // 삭제되었는지 확인
    }

    @Test
    @DisplayName("락 해제 실패 : 이미 없는 키")
    void releaseLock_WhenRequestIdDoesNotMatch_ShouldNotDeleteLock() {
        // given
        String lockKey = "lockKey";
        String requestId = "requestId";
        String differentRequestId = "differentRequestId";

        // dataRepository에서 반환하는 값이 requestId와 일치하지 않을 때
        when(dataRepository.findByKey(lockKey)).thenReturn(differentRequestId);

        // when
        redisDistributedLock.releaseLock(lockKey, requestId);

        // then
        verify(dataRepository, never()).deleteByKey(lockKey); // 삭제가 호출되지 않음을 검증
    }

    @Test
    @DisplayName("락 해제 실패 : 예외 발생")
    void releaseLock_WhenExceptionThrown_ShouldLogError() {
        // given
        String lockKey = "lockKey";
        String requestId = "requestId";

        // 예외가 발생하는 상황을 가정
        when(dataRepository.findByKey(lockKey)).thenThrow(new DataAccessException("Error") {});

        // when
        redisDistributedLock.releaseLock(lockKey, requestId);

        // then
        verify(dataRepository, never()).deleteByKey(lockKey); // 예외가 발생하면 삭제가 호출되지 않음을 검증
    }
}
