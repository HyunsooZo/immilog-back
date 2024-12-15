package com.backend.immilog.global.persistence;

import com.backend.immilog.global.infrastructure.persistence.config.QueryDSLConfig;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

@DisplayName("QueryDSLConfig 클래스 테스트")
class QueryDSLConfigTest {

    private final QueryDSLConfig queryDSLConfig = new QueryDSLConfig();
    private final EntityManager entityManager = mock(EntityManager.class);

    @Test
    @DisplayName("JPAQueryFactory 빈이 null이 아닌지 테스트")
    void jpaQueryFactoryIsNotNull() {
        JPAQueryFactory jpaQueryFactory = queryDSLConfig.jpaQueryFactory();
        assertNotNull(jpaQueryFactory);
    }

    @Test
    @DisplayName("JPAQueryFactory 빈이 올바르게 생성되는지 테스트")
    void jpaQueryFactoryIsCreatedCorrectly() {
        JPAQueryFactory jpaQueryFactory = queryDSLConfig.jpaQueryFactory();
        assertNotNull(jpaQueryFactory);
    }
}