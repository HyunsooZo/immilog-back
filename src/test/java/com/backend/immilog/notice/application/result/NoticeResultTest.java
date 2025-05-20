package com.backend.immilog.notice.application.result;

import com.backend.immilog.notice.application.dto.NoticeResult;
import com.backend.immilog.notice.domain.model.Notice;
import com.backend.immilog.global.enums.Country;
import com.backend.immilog.notice.domain.model.NoticeStatus;
import com.backend.immilog.notice.domain.model.NoticeType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("NoticeResult 클래스 테스트")
class NoticeResultTest {

    @Test
    @DisplayName("Notice 객체를 NoticeResult 객체로 변환")
    void fromNoticeToNoticeResult() {
        Notice notice = mock(Notice.class);
        when(notice.seq()).thenReturn(1L);
        when(notice.userSeq()).thenReturn(2L);
        when(notice.title()).thenReturn("Title");
        when(notice.content()).thenReturn("Content");
        when(notice.type()).thenReturn(NoticeType.NOTICE);
        when(notice.status()).thenReturn(NoticeStatus.NORMAL);
        when(notice.targetCountry()).thenReturn(List.of(Country.SOUTH_KOREA));
        when(notice.readUsers()).thenReturn(List.of(3L));
        when(notice.createdAt()).thenReturn(LocalDateTime.of(2023, 1, 1, 0, 0));

        NoticeResult result = NoticeResult.from(notice);

        assertThat(result.seq()).isEqualTo(1L);
        assertThat(result.authorUserSeq()).isEqualTo(2L);
        assertThat(result.title()).isEqualTo("Title");
        assertThat(result.content()).isEqualTo("Content");
        assertThat(result.type()).isEqualTo(NoticeType.NOTICE);
        assertThat(result.status()).isEqualTo(NoticeStatus.NORMAL);
        assertThat(result.targetCountry()).containsExactly(Country.SOUTH_KOREA);
        assertThat(result.readUsers()).containsExactly(3L);
        assertThat(result.createdAt()).isEqualTo(LocalDateTime.of(2023, 1, 1, 0, 0));
    }

    @Test
    @DisplayName("ResultSet을 NoticeResult 객체로 변환")
    void fromResultSetToNoticeResult() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getLong("seq")).thenReturn(1L);
        when(rs.getLong("user_seq")).thenReturn(2L);
        when(rs.getString("title")).thenReturn("Title");
        when(rs.getString("content")).thenReturn("Content");
        when(rs.getString("type")).thenReturn("NOTICE");
        when(rs.getString("status")).thenReturn("NORMAL");
        java.sql.Array targetCountryArray = mock(java.sql.Array.class);
        java.sql.Array readUsersArray = mock(java.sql.Array.class);
        when(rs.getArray("target_Country")).thenReturn(targetCountryArray);
        when(targetCountryArray.getArray()).thenReturn(new Country[]{Country.SOUTH_KOREA});
        when(rs.getArray("read_users")).thenReturn(readUsersArray);
        when(readUsersArray.getArray()).thenReturn(new Long[]{3L});
        when(rs.getTimestamp("created_at")).thenReturn(Timestamp.valueOf(LocalDateTime.of(2023, 1, 1, 0, 0)));

        NoticeResult result = NoticeResult.from(rs);

        assertThat(result.seq()).isEqualTo(1L);
        assertThat(result.authorUserSeq()).isEqualTo(2L);
        assertThat(result.title()).isEqualTo("Title");
        assertThat(result.content()).isEqualTo("Content");
        assertThat(result.type()).isEqualTo(NoticeType.NOTICE);
        assertThat(result.status()).isEqualTo(NoticeStatus.NORMAL);
        assertThat(result.targetCountry()).containsExactly(Country.SOUTH_KOREA);
        assertThat(result.readUsers()).containsExactly(3L);
        assertThat(result.createdAt()).isEqualTo(LocalDateTime.of(2023, 1, 1, 0, 0));
    }
}