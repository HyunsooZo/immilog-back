package com.backend.immilog.notice.application.dto;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.notice.domain.Notice;
import com.backend.immilog.notice.domain.NoticeStatus;
import com.backend.immilog.notice.domain.NoticeType;
import com.backend.immilog.notice.exception.NoticeErrorCode;
import com.backend.immilog.notice.exception.NoticeException;
import com.backend.immilog.notice.presentation.NoticeDetailResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public record NoticeModelResult(
        @Schema(description = "공지사항 번호") Long seq,
        @Schema(description = "작성자 번호") Long authorUserSeq,
        @Schema(description = "제목") String title,
        @Schema(description = "내용") String content,
        @Schema(description = "공지사항 타입") NoticeType type,
        @Schema(description = "공지사항 상태") NoticeStatus status,
        @Schema(description = "대상 국가") List<Country> targetCountry,
        @Schema(description = "읽은 사용자 목록") List<Long> readUsers,
        @Schema(description = "생성일") LocalDateTime createdAt
) {
    public static NoticeModelResult from(Notice notice) {
        return new NoticeModelResult(
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

    public static NoticeModelResult from(@NotNull ResultSet rs) {
        try {
            Array targetCountry = rs.getArray("target_Country");
            return new NoticeModelResult(
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

    public NoticeDetailResponse toResponse() {
        return new NoticeDetailResponse(HttpStatus.OK.value(), "success", this);
    }
}

