package com.backend.immilog.post.infrastructure.jdbc;

import com.backend.immilog.post.domain.enums.PostType;
import com.backend.immilog.post.domain.model.interaction.InteractionUser;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class InteractionUserJdbcRepository {
    private final JdbcClient jdbcClient;

    public InteractionUserJdbcRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<InteractionUser> findAllByPostSeqList(
            List<Long> postSeqList,
            PostType postType
    ) {
        if (postSeqList.isEmpty()) {
            return List.of();
        }

        String placeholders = postSeqList.stream()
                .map(seq -> "?")
                .collect(Collectors.joining(", "));

        String sql = """
                SELECT *
                FROM interaction_user
                WHERE post_seq IN (%s)
                AND post_type = :postType
                """.formatted(placeholders);

        return jdbcClient.sql(sql)
                .params(postSeqList.toArray())
                .param("postType", postType.name())
                .query(InteractionUser.class)
                .list();
    }
}
