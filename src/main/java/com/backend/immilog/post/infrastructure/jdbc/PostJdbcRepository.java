package com.backend.immilog.post.infrastructure.jdbc;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.post.domain.enums.Badge;
import com.backend.immilog.post.domain.enums.Categories;
import com.backend.immilog.post.domain.enums.PostStatus;
import com.backend.immilog.post.domain.enums.SortingMethods;
import com.backend.immilog.post.domain.model.post.Post;
import com.backend.immilog.post.infrastructure.jpa.entity.post.PostEntity;
import com.backend.immilog.post.infrastructure.jpa.entity.post.PostInfoValue;
import com.backend.immilog.post.infrastructure.jpa.entity.post.PostUserInfoValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
        List<String> conditions = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        conditions.add("p.is_public = ?");
        params.add(isPublic);

        if (category != Categories.ALL) {
            conditions.add("p.category = ?");
            params.add(category.name());
        }

        if (country != Country.ALL) {
            conditions.add("p.country = ?");
            params.add(country.name());
        }

        String whereClause = conditions.isEmpty() ? "" : "WHERE " + String.join(" AND ", conditions);
        String orderByClause = getOrderByClause(sortingMethod);

        String sql = String.format("""
                SELECT p.*, u.nickname, u.image_url
                FROM post p
                LEFT JOIN user u ON p.user_seq = u.seq
                %s
                %s
                LIMIT ? OFFSET ?
                """, whereClause, orderByClause);

        params.add(pageable.getPageSize());
        params.add(pageable.getOffset());

        List<PostEntity> postEntities = jdbcClient.sql(sql)
                .params(params.toArray())
                .query(POST_ENTITY_ROW_MAPPER)
                .list();

        String countSql = String.format("""
                SELECT COUNT(*)
                FROM post p
                LEFT JOIN user u ON p.user_seq = u.seq
                %s
                """, whereClause);

        int count = jdbcClient.sql(countSql)
                .params(params.subList(0, params.size() - 2).toArray())
                .query(Integer.class)
                .single();

        List<Post> posts = postEntities.stream().map(PostEntity::toDomain).toList();

        return new PageImpl<>(posts, pageable, count);
    }

    public Page<Post> getPostsByUserSeq(
            Long userSeq,
            Pageable pageable
    ) {
        String sql = """
                SELECT p.*, u.nickname, u.image_url
                FROM post p
                LEFT JOIN user u ON p.user_seq = u.seq
                WHERE p.user_seq = ?
                ORDER BY p.created_at DESC
                LIMIT ? OFFSET ?
                """;

        List<PostEntity> postEntities = jdbcClient.sql(sql)
                .param(userSeq)
                .param(pageable.getPageSize())
                .param(pageable.getOffset())
                .query(POST_ENTITY_ROW_MAPPER)
                .list();

        int count = jdbcClient.sql("""
                        SELECT COUNT(*)
                        FROM post p
                        LEFT JOIN user u ON p.user_seq = u.seq
                        WHERE p.user_seq = ?
                        """)
                .param(userSeq)
                .query(Integer.class)
                .single();

        List<Post> posts = postEntities.stream().map(PostEntity::toDomain).toList();

        return new PageImpl<>(posts, pageable, count);
    }

    public Page<Post> getPostsByKeyword(
            String keyword,
            Pageable pageable
    ) {
        String sql = """
                SELECT p.*, u.nickname, u.image_url
                FROM post p
                LEFT JOIN user u ON p.user_seq = u.seq
                WHERE p.content LIKE ? OR p.title LIKE ?
                LIMIT ? OFFSET ?
                """;

        List<PostEntity> postEntities = jdbcClient.sql(sql)
                .param("%" + keyword + "%")
                .param("%" + keyword + "%")
                .param(pageable.getPageSize())
                .param(pageable.getOffset())
                .query(POST_ENTITY_ROW_MAPPER)
                .list();

        String countSql = """
                SELECT COUNT(*)
                FROM post p
                LEFT JOIN user u ON p.user_seq = u.seq
                WHERE p.content LIKE ? OR p.title LIKE ?
                """;

        int count = jdbcClient.sql(countSql)
                .param("%" + keyword + "%")
                .param("%" + keyword + "%")
                .query(Integer.class)
                .single();

        List<Post> posts = postEntities.stream().map(PostEntity::toDomain).toList();

        return new PageImpl<>(posts, pageable, count);
    }

    public Optional<Post> getSinglePost(Long postSeq) {
        String sql = """
                SELECT p.*, u.nickname, u.image_url
                FROM post p
                LEFT JOIN user u ON p.user_seq = u.seq
                WHERE p.seq = ?
                """;

        return jdbcClient.sql(sql)
                .param(postSeq)
                .query(POST_ENTITY_ROW_MAPPER)
                .optional()
                .map(PostEntity::toDomain);
    }

    public List<Post> getPopularPosts(
            LocalDateTime from,
            LocalDateTime to,
            SortingMethods sortingMethods
    ) {
        String sql = String.format("""
                SELECT p.*, u.nickname, u.image_url
                FROM post p
                LEFT JOIN user u ON p.user_seq = u.seq
                WHERE p.created_at BETWEEN ? AND ?
                %s
                LIMIT 10
                """, getOrderByClause(sortingMethods));

        List<PostEntity> postEntities = jdbcClient.sql(sql)
                .param(from)
                .param(to)
                .query(POST_ENTITY_ROW_MAPPER)
                .list();

        return postEntities.stream().map(PostEntity::toDomain).toList();
    }

    private static final RowMapper<PostEntity> POST_ENTITY_ROW_MAPPER = (rs, rowNum) -> {
        Long seq = rs.getLong("seq");

        PostUserInfoValue postUserInfo = new PostUserInfoValue(
                rs.getLong("user_seq"),
                rs.getString("nickname"),
                rs.getString("image_url")
        );

        PostInfoValue postInfo = new PostInfoValue(
                rs.getString("title"),
                rs.getString("content"),
                getNullableLong(rs, "view_count"),
                getNullableLong(rs, "like_count"),
                rs.getString("region"),
                getEnum(rs, "status", PostStatus.class),
                getEnum(rs, "country", Country.class)
        );

        return new PostEntity(
                seq,
                postUserInfo.toDomain(),
                postInfo.toDomain(),
                getEnum(rs, "category", Categories.class),
                rs.getString("is_public"),
                getEnum(rs, "badge", Badge.class),
                getNullableLong(rs, "comment_count"),
                getNullableTimestamp(rs, "created_at"),
                getNullableTimestamp(rs, "updated_at")
        );
    };

    private static Long getNullableLong(
            ResultSet rs,
            String columnName
    ) throws SQLException {
        long value = rs.getLong(columnName);
        return rs.wasNull() ? null : value;
    }

    private static <T extends Enum<T>> T getEnum(
            ResultSet rs,
            String columnName,
            Class<T> enumClass
    ) throws SQLException {
        String value = rs.getString(columnName);
        return value != null ? Enum.valueOf(enumClass, value) : null;
    }

    private static LocalDateTime getNullableTimestamp(
            ResultSet rs,
            String columnName
    ) throws SQLException {
        return rs.getTimestamp(columnName) != null ? rs.getTimestamp(columnName).toLocalDateTime() : null;
    }

    private String getOrderByClause(SortingMethods sortingMethod) {
        String column = switch (sortingMethod) {
            case CREATED_DATE -> "p.created_at";
            case COMMENT_COUNT -> "p.comment_count";
            case LIKE_COUNT -> "p.like_count";
            case VIEW_COUNT -> "p.view_count";
        };
        return "ORDER BY " + column + " DESC ";
    }
}
