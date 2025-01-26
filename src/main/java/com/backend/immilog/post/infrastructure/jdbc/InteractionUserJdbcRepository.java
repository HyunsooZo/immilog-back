package com.backend.immilog.post.infrastructure.jdbc;

import com.backend.immilog.post.domain.model.interaction.InteractionUser;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InteractionUserJdbcRepository {
    private final JdbcClient jdbcClient;

    public InteractionUserJdbcRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<InteractionUser> findAllByPostSeqList(List<Long> postSeqList) {
        return jdbcClient.sql("""
                        SELECT *
                        FROM interaction_user
                        WHERE post_seq IN (:postSeqList)
                        """)
                .params(postSeqList)
                .query(InteractionUser.class)
                .list();
    }
}
