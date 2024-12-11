package com.backend.immilog.global.application;

import com.backend.immilog.global.infrastructure.repository.DataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService {
    private final DataRepository dataRepository;

    public void saveKeyAndValue(
            String key,
            String value,
            int expireTime
    ) {
        dataRepository.save(key, value, expireTime);
    }

    public String getValueByKey(String key) {
        return dataRepository.findByKey(key);
    }

    public void deleteValueByKey(String key) {
        dataRepository.deleteByKey(key);
    }
}

