package com.backend.immilog.global.persistence;

import com.backend.immilog.global.infrastructure.persistence.config.RedisConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

@DisplayName("RedisConfig 클래스 테스트")
class RedisConfigTest {

    private final RedisConfig redisConfig = new RedisConfig();
    private final RedisConnectionFactory redisConnectionFactory = mock(RedisConnectionFactory.class);

    private <T> void setFieldValue(
            Object target,
            String fieldName,
            T value
    ) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    @DisplayName("RedisConnectionFactory 빈이 null이 아닌지 테스트")
    void redisConnectionFactoryIsNotNull() throws Exception {
        setFieldValue(redisConfig, "host", "ko-meet-back.com");
        setFieldValue(redisConfig, "port", 6379);
        RedisConnectionFactory factory = redisConfig.redisConnectionFactory();
        assertNotNull(factory);
    }

    @Test
    @DisplayName("SortedSetTemplate 빈이 null이 아닌지 테스트")
    void sortedSetTemplateIsNotNull() {
        RedisTemplate<Object, Object> template = redisConfig.sortedSetTemplate(redisConnectionFactory);
        assertNotNull(template);
    }

    @Test
    @DisplayName("CacheManager 빈이 null이 아닌지 테스트")
    void cacheManagerIsNotNull() {
        RedisCacheManager cacheManager = redisConfig.cacheManager(redisConnectionFactory);
        assertNotNull(cacheManager);
    }
}