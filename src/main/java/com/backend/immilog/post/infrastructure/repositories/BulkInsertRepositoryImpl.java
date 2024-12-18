package com.backend.immilog.post.infrastructure.repositories;

import com.backend.immilog.post.domain.repositories.BulkInsertRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.IntStream;

@Slf4j
@Repository
public class BulkInsertRepositoryImpl implements BulkInsertRepository {
    private static final Map<String, String> TABLE_NAME_MAP =
            Map.of("post_resource", "post_resource");
    private final JdbcTemplate jdbcTemplate;
    private final Integer BATCH_SIZE = 500;

    public BulkInsertRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static String extractTableName(String sql) {
        for (String tableName : TABLE_NAME_MAP.keySet()) {
            if (sql.contains(tableName)) {
                return TABLE_NAME_MAP.get(tableName);
            }
        }
        return "Unknown Table";
    }

    @Override
    @Transactional
    public <T> void saveAll(
            List<T> entities,
            String sqlCommand,
            BiConsumer<PreparedStatement, T> setStatement
    ) {

        if (entities.isEmpty()) {
            return;
        }
        IntStream.range(0, (entities.size() + BATCH_SIZE - 1) / BATCH_SIZE)
                .mapToObj(i ->
                        entities.subList(
                                i * BATCH_SIZE,
                                Math.min(entities.size(), (i + 1) * BATCH_SIZE)
                        )
                ).forEach(batchList ->
                        jdbcTemplate.batchUpdate(
                                sqlCommand,
                                new BatchPreparedStatementSetter() {
                                    @Override
                                    public void setValues(
                                            PreparedStatement ps,
                                            int i
                                    ) {
                                        T entity = batchList.get(i);
                                        setStatement.accept(ps, entity);
                                    }

                                    @Override
                                    public int getBatchSize() {
                                        return batchList.size();
                                    }
                                })
                );
        log.info("{} has been inserted (count : {})", extractTableName(sqlCommand), entities.size());
    }

}

