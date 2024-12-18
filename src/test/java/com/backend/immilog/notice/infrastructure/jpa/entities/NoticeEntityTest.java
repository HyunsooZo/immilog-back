package com.backend.immilog.notice.infrastructure.jpa.entities;

import com.backend.immilog.notice.domain.model.Notice;
import com.backend.immilog.notice.domain.model.enums.NoticeCountry;
import com.backend.immilog.notice.domain.model.enums.NoticeStatus;
import com.backend.immilog.notice.domain.model.enums.NoticeType;
import com.backend.immilog.notice.infrastructure.jpa.NoticeEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("NoticeEntity 테스트")
class NoticeEntityTest {

    @Test
    @DisplayName("NoticeEntity 생성 테스트")
    void from_createsNoticeEntityFromNotice() {
        Notice notice = Notice.builder()
                .seq(1L)
                .userSeq(2L)
                .title("Title")
                .content("Content")
                .type(NoticeType.NOTICE)
                .status(NoticeStatus.NORMAL)
                .targetCountries(List.of(NoticeCountry.SINGAPORE))
                .readUsers(List.of(3L))
                .build();

        NoticeEntity noticeEntity = NoticeEntity.from(notice);
        Notice domain = noticeEntity.toDomain();

        assertThat(domain.getSeq()).isEqualTo(notice.getSeq());
        assertThat(domain.getUserSeq()).isEqualTo(notice.getUserSeq());
        assertThat(domain.getTitle()).isEqualTo(notice.getTitle());
        assertThat(domain.getContent()).isEqualTo(notice.getContent());
        assertThat(domain.getType()).isEqualTo(notice.getType());
        assertThat(domain.getStatus()).isEqualTo(notice.getStatus());
        assertThat(domain.getTargetCountries()).isEqualTo(notice.getTargetCountries());
        assertThat(domain.getReadUsers()).isEqualTo(notice.getReadUsers());
    }

    @Test
    @DisplayName("NoticeEntity toDomain 메서드 테스트")
    void toDomain_createsNoticeFromNoticeEntity() {
        NoticeEntity noticeEntity = NoticeEntity.builder()
                .seq(1L)
                .userSeq(2L)
                .title("Title")
                .content("Content")
                .type(NoticeType.NOTICE)
                .status(NoticeStatus.NORMAL)
                .targetCountries(List.of(NoticeCountry.SINGAPORE))
                .readUsers(List.of(3L))
                .build();

        Notice notice = noticeEntity.toDomain();

        assertThat(notice.getSeq()).isEqualTo(1L);
        assertThat(notice.getUserSeq()).isEqualTo(2L);
        assertThat(notice.getTitle()).isEqualTo("Title");
        assertThat(notice.getContent()).isEqualTo("Content");
        assertThat(notice.getType()).isEqualTo(NoticeType.NOTICE);
        assertThat(notice.getStatus()).isEqualTo(NoticeStatus.NORMAL);
        assertThat(notice.getTargetCountries()).isEqualTo(List.of(NoticeCountry.SINGAPORE));
        assertThat(notice.getReadUsers()).isEqualTo(List.of(3L));
    }

    @Test
    @DisplayName("NoticeEntity null 값 처리 테스트")
    void from_handlesNullValues() {
        Notice notice = Notice.builder()
                .seq(null)
                .userSeq(null)
                .title(null)
                .content(null)
                .type(null)
                .status(null)
                .targetCountries(null)
                .readUsers(null)
                .build();

        NoticeEntity noticeEntity = NoticeEntity.from(notice);
    }

    @Test
    @DisplayName("NoticeEntity toDomain null 값 처리 테스트")
    void toDomain_handlesNullValues() {
        NoticeEntity noticeEntity = NoticeEntity.builder()
                .seq(null)
                .userSeq(null)
                .title(null)
                .content(null)
                .type(null)
                .status(null)
                .targetCountries(null)
                .readUsers(null)
                .build();

        Notice notice = noticeEntity.toDomain();

        assertThat(notice.getSeq()).isNull();
        assertThat(notice.getUserSeq()).isNull();
        assertThat(notice.getTitle()).isNull();
        assertThat(notice.getContent()).isNull();
        assertThat(notice.getType()).isNull();
        assertThat(notice.getStatus()).isNull();
        assertThat(notice.getTargetCountries()).isNull();
        assertThat(notice.getReadUsers()).isNull();
    }

    @Test
    @DisplayName("NoticeEntity Setter 테스트")
    void setter_test() {
        NoticeEntity noticeEntity = NoticeEntity.builder()
                .title("Title")
                .content("Content")
                .type(NoticeType.NOTICE)
                .status(NoticeStatus.NORMAL)
                .build();
    }
}