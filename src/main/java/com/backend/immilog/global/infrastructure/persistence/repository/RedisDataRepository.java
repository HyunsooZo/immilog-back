package com.backend.immilog.global.infrastructure.persistence.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

@Repository
public class RedisDataRepository implements DataRepository {
    private final RedisTemplate<String, String> stringRedisTemplate;

    public RedisDataRepository(RedisTemplate<String, String> stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void save(
            String key,
            String value,
            int expireTime
    ) {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set(key, value);
        stringRedisTemplate.expire(key, expireTime, MINUTES);
    }

    @Override
    public String findByKey(String refreshToken) {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        return ops.get(refreshToken);
    }

    @Override
    public void deleteByKey(String refreshToken) {
        stringRedisTemplate.delete(refreshToken);
    }

    @Override
    public Boolean saveIfAbsent(
            String key,
            String value,
            long expireTimeInSeconds
    ) {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        return ops.setIfAbsent(key, value, expireTimeInSeconds, SECONDS);
    }
}
