package com.backend.immilog.post.domain.repositories;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.function.BiConsumer;

public interface BulkInsertRepository {
    <T> void saveAll(
            List<T> entities,
            String sqlCommand,
            BiConsumer<PreparedStatement, T> setStatement
    );
}


