package com.backend.immilog.notice.infrastructure.jdbc;

import com.backend.immilog.notice.application.dto.NoticeModelResult;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NoticeJdbcRepository {
    private final JdbcClient jdbcClient;

    public NoticeJdbcRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<NoticeModelResult> getNotices(
            long userSeq,
            int pageSize,
            long offset
    ) {
        String sql = """
                SELECT n.*
                FROM notice n
                LEFT JOIN notice_entity_target_country ntc ON n.seq = ntc.notice_entity_seq
                LEFT JOIN user u ON u.country = ntc.target_country 
                LEFT JOIN notice_entity_read_users nru ON n.seq = nru.notice_entity_seq
                WHERE (u.country = ntc.target_country
                   OR ntc.target_country = 'ALL')
                   AND n.status = 'NORMAL'
                   AND (nru.read_users IS NULL OR nru.read_users != ?)
                ORDER BY n.created_at DESC
                LIMIT ? OFFSET ?
                """;

        return jdbcClient.sql(sql)
                .param(userSeq)
                .param(pageSize)
                .param(offset)
                .query((rs, rowNum) -> NoticeModelResult.from(rs))
                .list();
    }

    public Long getTotal(Long userSeq) {
        String sql = """
                SELECT COUNT(*) 
                FROM notice n
                LEFT JOIN immilog.notice_entity_target_country ntc ON n.seq = ntc.notice_entity_seq
                LEFT JOIN user u ON u.country = ntc.target_country 
                LEFT JOIN notice_entity_read_users nru ON n.seq = nru.notice_entity_seq
                WHERE (u.country = ntc.target_country
                   OR ntc.target_country = 'ALL')
                   AND n.status = 'NORMAL'
                   AND (nru.read_users IS NULL OR nru.read_users != ?)
                """;
        try {
            return jdbcClient.sql(sql)
                    .param(userSeq)
                    .query((rs, rowNum) -> rs.getLong(1))
                    .single();
        } catch (EmptyResultDataAccessException e) {
            return 0L;
        }
    }
}
