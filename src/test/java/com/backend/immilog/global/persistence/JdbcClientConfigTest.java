package com.backend.immilog.global.persistence;

import com.backend.immilog.global.infrastructure.persistence.config.JdbcClientConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.simple.JdbcClient;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

@DisplayName("JdbcClientConfig 클래스 테스트")
class JdbcClientConfigTest {

    private final JdbcClientConfig jdbcClientConfig = new JdbcClientConfig();
    private final DataSource dataSource = mock(DataSource.class);

    @Test
    @DisplayName("JdbcClient 빈이 null이 아닌지 테스트")
    void jdbcClientIsNotNull() {
        JdbcClient jdbcClient = jdbcClientConfig.jdbcClient(dataSource);
        assertNotNull(jdbcClient);
    }

    @Test
    @DisplayName("JdbcClient 빈이 올바르게 생성되는지 테스트")
    void jdbcClientIsCreatedCorrectly() {
        JdbcClient jdbcClient = jdbcClientConfig.jdbcClient(dataSource);
        assertNotNull(jdbcClient);
    }
}