package com.backend.immilog.global.infrastructure.repository;

public interface DataRepository {
    void save(
            String key,
            String value,
            int expireTime
    );

    String findByKey(String key);

    void deleteByKey(String key);

    Boolean saveIfAbsent(
            String key,
            String value,
            long expireTimeInSeconds
    );

}
