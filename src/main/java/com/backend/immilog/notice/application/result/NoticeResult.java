package com.backend.immilog.notice.application.result;

import com.backend.immilog.notice.domain.model.Notice;
import com.backend.immilog.notice.domain.model.enums.NoticeCountry;
import com.backend.immilog.notice.domain.model.enums.NoticeStatus;
import com.backend.immilog.notice.domain.model.enums.NoticeType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Builder
public record NoticeResult(
        Long seq,
        Long authorUserSeq,
        String title,
        String content,
        NoticeType type,
        NoticeStatus status,
        List<NoticeCountry> targetCountries,
        List<Long> readUsers,
        LocalDateTime createdAt
) {
    public static NoticeResult from(
            Notice notice
    ) {
        return NoticeResult.builder()
                .seq(notice.getSeq())
                .authorUserSeq(notice.getUserSeq())
                .title(notice.getTitle())
                .content(notice.getContent())
                .type(notice.getType())
                .status(notice.getStatus())
                .targetCountries(notice.getTargetCountries())
                .readUsers(notice.getReadUsers())
                .createdAt(notice.getCreatedAt())
                .build();
    }

    public static NoticeResult from(
            @NotNull ResultSet rs
    ) throws SQLException {
        Array targetCountries1 = rs.getArray("target_countries");
        return new NoticeResult(
                rs.getLong("seq"),
                rs.getLong("user_seq"),
                rs.getString("title"),
                rs.getString("content"),
                NoticeType.valueOf(rs.getString("type")),
                NoticeStatus.valueOf(rs.getString("status")),
                Arrays.asList((NoticeCountry[]) targetCountries1.getArray()),
                Arrays.asList((Long[]) rs.getArray("read_users").getArray()),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}

