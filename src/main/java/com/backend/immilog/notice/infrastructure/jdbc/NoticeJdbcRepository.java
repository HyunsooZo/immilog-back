package com.backend.immilog.notice.infrastructure.jdbc;

import com.backend.immilog.notice.application.result.NoticeResult;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NoticeJdbcRepository {
    private final JdbcClient jdbcClient;

    public NoticeJdbcRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<NoticeResult> getNotices(
            long userSeq,
            int pageSize,
            long offset
    ) {
        String sql = """
                SELECT n.*
                FROM notice n
                LEFT JOIN notice_entity_target_countries ntc ON n.seq = ntc.notice_entity_seq
                LEFT JOIN user u ON u.country = ntc.target_countries 
                LEFT JOIN notice_entity_read_users nru ON n.seq = nru.notice_entity_seq
                WHERE (u.country = ntc.target_countries
                   OR ntc.target_countries = 'ALL')
                   AND n.status = 'NORMAL'
                   AND (nru.read_users IS NULL OR nru.read_users != ?)
                ORDER BY n.created_at DESC
                LIMIT ? OFFSET ?
                """;

        return jdbcClient.sql(sql)
                .param(userSeq)
                .param(pageSize)
                .param(offset)
                .query((rs, rowNum) -> NoticeResult.from(rs))
                .list();
    }

    public Long getTotal(
            Long userSeq
    ) {
        String sql = """
                SELECT n.*
                FROM notice n
                LEFT JOIN notice_entity_target_Country ntc ON n.seq = ntc.notice_entity_seq
                LEFT JOIN user u ON u.country = ntc.target_Country 
                LEFT JOIN notice_entity_read_users nru ON n.seq = nru.notice_entity_seq
                WHERE (u.country = ntc.target_Country
                   OR ntc.target_Country = 'ALL')
                   AND n.status = 'NORMAL'
                   AND (nru.read_users IS NULL OR nru.read_users != ?)
                """;
        return jdbcClient.sql(sql)
                .param(userSeq)
                .query((rs, rowNum) -> rs.getLong(1))
                .single();
    }

}
