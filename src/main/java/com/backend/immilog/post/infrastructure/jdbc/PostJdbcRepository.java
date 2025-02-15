package com.backend.immilog.post.infrastructure.jdbc;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.post.domain.enums.Categories;
import com.backend.immilog.post.domain.enums.SortingMethods;
import com.backend.immilog.post.domain.model.post.Post;
import com.backend.immilog.post.infrastructure.jpa.entity.post.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class PostJdbcRepository {
    private final JdbcClient jdbcClient;

    public PostJdbcRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Page<Post> getPosts(
            Country country,
            SortingMethods sortingMethod,
            String isPublic,
            Categories category,
            Pageable pageable
    ) {
        String sql = """
            SELECT * 
            FROM post
            WHERE country = ?
            AND is_public = ?
            AND category = ?
            """ + this.getOrderByClause(sortingMethod) + """
            LIMIT ?
            OFFSET ?
            """;

        List<PostEntity> posts = jdbcClient.sql(sql)
                .param(country.name())
                .param(isPublic)
                .param(category.name())
                .param(pageable.getPageSize())
                .param(pageable.getOffset())
                .query(PostEntity.class)
                .list();

        int count = jdbcClient.sql("""
                    SELECT COUNT(*) 
                    FROM post
                    WHERE country = ?
                    AND is_public = ?
                    AND category = ?
                    """)
                .param(country.name())
                .param(isPublic)
                .param(category.name())
                .query(Integer.class)
                .single();

        return new PageImpl<>(
                posts.stream().map(PostEntity::toDomain).toList(),
                pageable,
                count
        );
    }

    public Page<Post> getPostsByUserSeq(
            Long userSeq,
            Pageable pageable
    ) {
        String sql = """
                SELECT * 
                FROM post
                WHERE user_seq = ?
                ORDER BY created_at DESC
                LIMIT ?
                OFFSET ?
                """;

        List<PostEntity> posts = jdbcClient.sql(sql)
                .param(userSeq)
                .param(pageable.getPageSize())
                .param(pageable.getOffset())
                .query(PostEntity.class)
                .list();

        int count = jdbcClient.sql("""
                        SELECT COUNT(*) 
                        FROM post
                        WHERE user_seq = ?
                        """)
                .param(userSeq)
                .query(Integer.class)
                .single();

        return new PageImpl<>(
                posts.stream().map(PostEntity::toDomain).toList(),
                pageable,
                count
        );
    }

    private String getOrderByClause(SortingMethods sortingMethod) {
        String column = switch (sortingMethod) {
            case CREATED_DATE -> "created_at";
            case COMMENT_COUNT -> "comment_count";
            case LIKE_COUNT -> "like_count";
            case VIEW_COUNT -> "view_count";
        };
        return "ORDER BY " + column + " DESC ";
    }

    public Page<Post> getPostsByKeyword(
            String keyword,
            Pageable pageable
    ) {
        String sql = """
                SELECT * 
                FROM post
                WHERE content LIKE ?
                OR title LIKE ?
                LIMIT ?
                OFFSET ?
                """;
        List<PostEntity> posts = jdbcClient.sql(sql)
                .param("%" + keyword + "%")
                .param("%" + keyword + "%")
                .param(pageable.getPageSize())
                .param(pageable.getOffset())
                .query(PostEntity.class)
                .list();

        int count = jdbcClient.sql("""
                        SELECT COUNT(*) 
                        FROM post
                        WHERE content LIKE ?
                        OR title LIKE ?
                        """)
                .param("%" + keyword + "%")
                .param("%" + keyword + "%")
                .query(Integer.class)
                .single();

        return new PageImpl<>(
                posts.stream().map(PostEntity::toDomain).toList(),
                pageable,
                count
        );
    }

    public Optional<Post> getSinglePost(Long postSeq) {
        String sql = """
                SELECT * 
                FROM post
                WHERE seq = ?
                """;

        return Optional.of(jdbcClient.sql(sql)
                .param(postSeq)
                .query(PostEntity.class)
                .single()
                .toDomain());
    }

    public List<Post> getPopularPosts(
            LocalDateTime from,
            LocalDateTime to,
            SortingMethods sortingMethods
    ) {
        String sql = """
                SELECT * 
                FROM post
                WHERE created_at BETWEEN ? AND ?
                """ + this.getOrderByClause(sortingMethods) + """
                LIMIT 10
                """;

        return jdbcClient.sql(sql)
                .param(from)
                .param(to)
                .query(PostEntity.class)
                .list()
                .stream()
                .map(PostEntity::toDomain)
                .toList();
    }
}