package com.backend.immilog.post.infrastructure.jdbc;

import com.backend.immilog.post.domain.enums.PostType;
import com.backend.immilog.post.domain.enums.ResourceType;
import com.backend.immilog.post.domain.model.resource.PostResource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class PostResourceJdbcRepository {
    private final JdbcClient jdbcClient;

    public PostResourceJdbcRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public void deleteAllEntities(
            Long postSeq,
            PostType postType,
            ResourceType resourceType,
            List<String> deleteAttachments
    ) {
        String inClause = deleteAttachments.stream()
                .map(item -> "?")
                .collect(Collectors.joining(", "));

        String sql = """
                DELETE FROM post_resource_entity
                WHERE post_seq = ?
                AND post_type = ?
                AND resource_type = ?
                AND resource_name IN (%s)
                """.formatted(inClause);

        jdbcClient.sql(sql)
                .param(postSeq)
                .param(postType.toString())
                .param(resourceType.toString())
                .params(deleteAttachments.toArray())
                .update();
    }

    public void deleteAllByPostSeq(Long seq) {
        jdbcClient.sql("""
                        DELETE FROM post_resource
                        WHERE post_seq = ?
                        """)
                .param(seq)
                .update();
    }

    public List<PostResource> findAllByPostSeqList(
            List<Long> postSeqList,
            PostType postType
    ) {
        return jdbcClient.sql("""
                        SELECT *
                        FROM post_resource
                        WHERE post_seq IN (:postSeqList)
                        AND post_type = :postType
                        """)
                .params(postSeqList)
                .query(PostResource.class)
                .list();
    }
}
