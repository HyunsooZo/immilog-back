package com.backend.immilog.post.infrastructure.jdbc;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.post.domain.enums.Experience;
import com.backend.immilog.post.domain.enums.Industry;
import com.backend.immilog.post.infrastructure.jpa.entity.post.JobBoardEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JobBoardJdbcRepository {
    private final JdbcClient jdbcClient;

    public JobBoardJdbcRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<JobBoardEntity> getJobBoards(
            Country country,
            String sortingMethod,
            Industry industry,
            Experience experience,
            Pageable pageable
    ) {
        String sql = """
                SELECT * 
                FROM job_board
                WHERE country = ?
                AND industry = ?
                AND experience = ?
                ORDER BY """ + sortingMethod + """
                LIMIT ?
                OFFSET ?
                """;
        return jdbcClient.sql(sql)
                .param(country)
                .param(industry)
                .param(experience)
                .param(pageable.getPageSize())
                .param(pageable.getOffset())
                .query(JobBoardEntity.class)
                .list();

    }

    public Integer getTotal(
            Country country,
            Industry industry,
            Experience experience
    ) {
        return jdbcClient.sql("""
                        SELECT COUNT(*) 
                        FROM job_board
                        WHERE country = ?
                        AND industry = ?
                        AND experience = ?
                        """)
                .param(country)
                .param(industry)
                .param(experience)
                .query(Integer.class)
                .single();

    }
}
