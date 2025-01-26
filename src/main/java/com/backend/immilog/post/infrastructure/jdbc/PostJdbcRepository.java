package com.backend.immilog.post.infrastructure.jdbc;

import com.backend.immilog.post.application.result.PostResult;
import com.backend.immilog.post.domain.enums.Categories;
import com.backend.immilog.post.domain.enums.Countries;
import com.backend.immilog.post.domain.enums.SortingMethods;
import com.backend.immilog.post.domain.model.post.Post;
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

    public PostJdbcRepository(JdbcClient jdbcClient) {this.jdbcClient = jdbcClient;}

    public Page<PostResult> getPostResults(
            Countries country,
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
                """ + getOrderByClause(sortingMethod) + """
                LIMIT ?
                OFFSET ?
                """;

        List<Post> posts = jdbcClient.sql(sql)
                .param(country)
                .param(isPublic)
                .param(category)
                .param(pageable.getPageSize())
                .param(pageable.getOffset())
                .query(Post.class)
                .list();

        int count = jdbcClient.sql("""
                        SELECT COUNT(*) 
                        FROM post
                        WHERE country = ?
                        AND is_public = ?
                        AND category = ?
                        """)
                .param(country)
                .param(isPublic)
                .param(category)
                .query(Integer.class)
                .single();

        return new PageImpl<>(
                posts.stream().map(Post::toResult).toList(),
                pageable,
                count
        );
    }

    public Page<PostResult> getPostsByUserSeq(
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

        List<Post> posts = jdbcClient.sql(sql)
                .param(userSeq)
                .param(pageable.getPageSize())
                .param(pageable.getOffset())
                .query(Post.class)
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
                posts.stream().map(Post::toResult).toList(),
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
        return """
                ORDER BY %s DESC
                """.formatted(column);
    }

    public Page<PostResult> getPostsByKeyword(
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
        List<Post> posts = jdbcClient.sql(sql)
                .param("%" + keyword + "%")
                .param("%" + keyword + "%")
                .param(pageable.getPageSize())
                .param(pageable.getOffset())
                .query(Post.class)
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
                posts.stream().map(Post::toResult).toList(),
                pageable,
                count
        );
    }

    public Optional<PostResult> getSinglePost(Long postSeq) {
        String sql = """
                SELECT * 
                FROM post
                WHERE seq = ?
                """;

        return Optional.of(jdbcClient.sql(sql)
                .param(postSeq)
                .query(Post.class)
                .single()
                .toResult());
    }

    public List<PostResult> getPopularPosts(
            LocalDateTime from,
            LocalDateTime to,
            SortingMethods sortingMethods
    ) {
        String sql = """
                SELECT * 
                FROM post
                WHERE created_at BETWEEN ? AND ?
                ORDER BY %s DESC
                LIMIT 10
                """.formatted(getOrderByClause(sortingMethods));

        return jdbcClient.sql(sql)
                .param(from)
                .param(to)
                .query(Post.class)
                .list()
                .stream()
                .map(Post::toResult)
                .toList();
    }
}