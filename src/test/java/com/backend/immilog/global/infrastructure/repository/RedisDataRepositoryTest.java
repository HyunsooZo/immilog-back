package com.backend.immilog.global.infrastructure.repository;

import com.backend.immilog.global.infrastructure.persistence.repository.RedisDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@DisplayName("RedisDataRepository 클래스 테스트")
class RedisDataRepositoryTest {

    private final RedisTemplate<String, String> stringRedisTemplate = mock(RedisTemplate.class);
    private final ValueOperations<String, String> valueOperations = mock(ValueOperations.class);
    private final RedisDataRepository redisDataRepository = new RedisDataRepository(stringRedisTemplate);

    @BeforeEach
    void setUp() {
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    @DisplayName("데이터가 성공적으로 저장되는지 테스트")
    void dataIsSavedSuccessfully() {
        String key = "testKey";
        String value = "testValue";
        int expireTime = 10;
        redisDataRepository.save(key, value, expireTime);
        verify(valueOperations, times(1)).set(key, value);
        verify(stringRedisTemplate, times(1)).expire(key, expireTime, MINUTES);
    }

    @Test
    @DisplayName("키로 데이터를 성공적으로 찾는지 테스트")
    void dataIsFoundByKeySuccessfully() {
        String key = "testKey";
        String expectedValue = "testValue";
        when(valueOperations.get(key)).thenReturn(expectedValue);
        String actualValue = redisDataRepository.findByKey(key);
        assertEquals(expectedValue, actualValue);
    }

    @Test
    @DisplayName("키로 데이터를 찾을 때 null을 반환하는지 테스트")
    void dataIsNullWhenKeyNotFound() {
        String key = "nonExistentKey";
        when(valueOperations.get(key)).thenReturn(null);
        String actualValue = redisDataRepository.findByKey(key);
        assertNull(actualValue);
    }

    @Test
    @DisplayName("키로 데이터를 성공적으로 삭제하는지 테스트")
    void dataIsDeletedByKeySuccessfully() {
        String key = "testKey";
        redisDataRepository.deleteByKey(key);
        verify(stringRedisTemplate, times(1)).delete(key);
    }

    @Test
    @DisplayName("데이터가 존재하지 않을 때 성공적으로 저장되는지 테스트")
    void dataIsSavedIfAbsentSuccessfully() {
        String key = "testKey";
        String value = "testValue";
        long expireTimeInSeconds = 600;
        when(valueOperations.setIfAbsent(key, value, expireTimeInSeconds, SECONDS)).thenReturn(true);
        Boolean result = redisDataRepository.saveIfAbsent(key, value, expireTimeInSeconds);
        assertEquals(true, result);
    }

    @Test
    @DisplayName("데이터가 존재할 때 저장되지 않는지 테스트")
    void dataIsNotSavedIfAlreadyExists() {
        String key = "testKey";
        String value = "testValue";
        long expireTimeInSeconds = 600;
        when(valueOperations.setIfAbsent(key, value, expireTimeInSeconds, SECONDS)).thenReturn(false);
        Boolean result = redisDataRepository.saveIfAbsent(key, value, expireTimeInSeconds);
        assertEquals(false, result);
    }
}