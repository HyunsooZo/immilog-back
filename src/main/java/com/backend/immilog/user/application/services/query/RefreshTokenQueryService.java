package com.backend.immilog.user.application.services.query;

import com.backend.immilog.global.infrastructure.persistence.repository.DataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenQueryService {
    private final DataRepository dataRepository;

    public String getValueByKey(String key) {
        return dataRepository.findByKey(key);
    }
}
