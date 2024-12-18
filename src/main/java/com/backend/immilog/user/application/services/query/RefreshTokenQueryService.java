package com.backend.immilog.user.application.services.query;

import com.backend.immilog.global.infrastructure.persistence.repository.DataRepository;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenQueryService {
    private final DataRepository dataRepository;

    public RefreshTokenQueryService(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public String getValueByKey(String key) {
        return dataRepository.findByKey(key);
    }
}
