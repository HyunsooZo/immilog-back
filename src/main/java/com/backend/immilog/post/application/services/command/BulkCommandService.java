package com.backend.immilog.post.application.services.command;

import com.backend.immilog.post.domain.repositories.BulkInsertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.function.BiConsumer;

@Service
@RequiredArgsConstructor
public class BulkCommandService {
    private final BulkInsertRepository bulkInsertRepository;

    @Transactional
    public <T> void saveAll(
            List<T> entities,
            String command,
            BiConsumer<PreparedStatement, T> failedToSavePostResource
    ) {
        bulkInsertRepository.saveAll(
                entities,
                command,
                failedToSavePostResource
        );
    }
}
