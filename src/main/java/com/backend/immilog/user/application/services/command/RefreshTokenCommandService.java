package com.backend.immilog.user.application.services.command;

import com.backend.immilog.global.infrastructure.persistence.repository.DataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenCommandService {
    private final DataRepository dataRepository;

    public void saveKeyAndValue(
            String key,
            String value,
            int expireTime
    ) {
        dataRepository.save(key, value, expireTime);
    }

    public void deleteValueByKey(String key) {
        dataRepository.deleteByKey(key);
    }
}
