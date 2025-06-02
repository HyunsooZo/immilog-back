package com.backend.immilog.post.infrastructure.jdbc;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.global.enums.UserRole;
import com.backend.immilog.post.application.result.CommentResult;
import com.backend.immilog.post.domain.model.post.PostStatus;
import com.backend.immilog.post.exception.PostErrorCode;
import com.backend.immilog.post.exception.PostException;
import com.backend.immilog.user.application.result.UserInfoResult;
import com.backend.immilog.user.domain.model.user.UserStatus;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

@Repository
public class CommentJdbcRepository {
    private final JdbcClient jdbcClient;

    public CommentJdbcRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    private static CommentResult getCommentEntityResult(
            ResultSet rs,
            long commentSeq,
            UserInfoResult user
    ) {
        try {
            final var prefix = (commentSeq == rs.getLong("c.seq")) ? "c" : "cc";
            final var content = rs.getString(prefix + ".content");
            final int likeCount = rs.getInt(prefix + ".like_count");
            final int replyCount = rs.getInt(prefix + ".reply_count");
            final var status = PostStatus.valueOf(rs.getString(prefix + ".status"));
            final var localDateTime = rs.getTimestamp(prefix + ".created_at").toLocalDateTime();

            return new CommentResult(
                    commentSeq,
                    user,
                    content,
                    new ArrayList<>(),
                    likeCount,
                    0,
                    replyCount,
                    new ArrayList<>(),
                    status,
                    localDateTime.toString()
            );
        } catch (SQLException e) {
            throw new PostException(PostErrorCode.COMMENT_NOT_FOUND);
        }
    }

    private static UserInfoResult getUserInfoResult(
            ResultSet rs,
            String prefix
    ) {
        try {
            return new UserInfoResult(
                    rs.getLong(prefix + ".seq"),
                    rs.getString(prefix + ".nickname"),
                    rs.getString(prefix + ".email"),
                    rs.getString(prefix + ".image_url"),
                    rs.getLong(prefix + ".reported_count"),
                    rs.getDate(prefix + ".reported_date"),
                    Country.valueOf(rs.getString(prefix + ".country")),
                    Country.valueOf(rs.getString(prefix + ".interest_country")),
                    rs.getString(prefix + ".region"),
                    UserRole.valueOf(rs.getString(prefix + ".user_role")),
                    UserStatus.valueOf(rs.getString(prefix + ".user_status"))
            );
        } catch (SQLException e) {
            throw new PostException(PostErrorCode.COMMENT_NOT_FOUND);
        }
    }

    public List<CommentResult> getComments(Long postSeq) {
        var sql = """
                SELECT c.*, u.*, cc.*, cu.*
                FROM comment c
                LEFT JOIN user u ON c.user_seq = u.seq
                LEFT JOIN comment cc ON cc.post_seq = ?
                                    AND cc.parent_seq = c.seq
                                    AND cc.reference_type = 'COMMENT'
                LEFT JOIN user cu ON cc.user_seq = cu.seq
                WHERE c.post_seq = ?
                    AND c.parent_seq IS NULL
                    AND c.reference_type = 'POST'
                ORDER BY c.created_at DESC
                """;

        return jdbcClient.sql(sql)
                .param(postSeq)
                .param(postSeq)
                .query((rs, rowNum) -> mapParentCommentWithChildren(rs))
                .stream()
                .flatMap(List::stream)
                .toList();
    }

    private List<CommentResult> mapParentCommentWithChildren(ResultSet rs) {
        var parentMap = new LinkedHashMap<Long, CommentResult>();
        var childrenMap = new LinkedHashMap<Long, List<CommentResult>>();

        try {
            do {
                long parentSeq = rs.getLong("c.seq");
                parentMap.computeIfAbsent(parentSeq, key -> getCommentEntityResult(rs, parentSeq, getUserInfoResult(rs, "u")));

                long childSeq = rs.getLong("cc.seq");
                if (childSeq != 0) {
                    var childComment = getCommentEntityResult(rs, childSeq, getUserInfoResult(rs, "cu"));
                    childrenMap.computeIfAbsent(parentSeq, k -> new ArrayList<>()).add(childComment);
                }
            } while (rs.next());
        } catch (Exception e) {
            throw new PostException(PostErrorCode.COMMENT_NOT_FOUND);
        }

        var resultList = new ArrayList<CommentResult>();
        for (var parentSeq : parentMap.keySet()) {
            var parent = parentMap.get(parentSeq);
            var childList = childrenMap.getOrDefault(parentSeq, Collections.emptyList());

            var combined = new CommentResult(
                    parent.seq(),
                    parent.user(),
                    parent.content(),
                    childList,              // 여기에 미리 수집한 자식 리스트를 넣는다
                    parent.upVotes(),
                    parent.downVotes(),
                    parent.replyCount(),
                    parent.likeUsers(),
                    parent.status(),
                    parent.createdAt()
            );
            resultList.add(combined);
        }

        return resultList;
    }
}
