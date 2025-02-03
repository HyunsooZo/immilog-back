package com.backend.immilog.notice.application.result;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.notice.domain.model.Notice;
import com.backend.immilog.notice.domain.model.enums.NoticeStatus;
import com.backend.immilog.notice.domain.model.enums.NoticeType;
import com.backend.immilog.notice.exception.NoticeErrorCode;
import com.backend.immilog.notice.exception.NoticeException;
import jakarta.validation.constraints.NotNull;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public record NoticeResult(
        Long seq,
        Long authorUserSeq,
        String title,
        String content,
        NoticeType type,
        NoticeStatus status,
        List<Country> targetCountry,
        List<Long> readUsers,
        LocalDateTime createdAt
) {
    public static NoticeResult from(Notice notice) {
        return new NoticeResult(
                notice.seq(),
                notice.userSeq(),
                notice.title(),
                notice.content(),
                notice.type(),
                notice.status(),
                notice.targetCountry(),
                notice.readUsers(),
                notice.createdAt()
        );
    }

    public static NoticeResult from(@NotNull ResultSet rs) {
        try {
            Array targetCountry = rs.getArray("target_Country");
        return new NoticeResult(
                rs.getLong("seq"),
                rs.getLong("user_seq"),
                rs.getString("title"),
                rs.getString("content"),
                NoticeType.valueOf(rs.getString("type")),
                NoticeStatus.valueOf(rs.getString("status")),
                Arrays.asList((Country[]) targetCountry.getArray()),
                Arrays.asList((Long[]) rs.getArray("read_users").getArray()),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
        } catch (SQLException e) {
            throw new NoticeException(NoticeErrorCode.SQL_ERROR);
        }
    }
}

