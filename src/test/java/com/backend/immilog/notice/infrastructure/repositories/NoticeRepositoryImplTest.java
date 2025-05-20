package com.backend.immilog.notice.infrastructure.repositories;

import com.backend.immilog.notice.application.dto.NoticeResult;
import com.backend.immilog.notice.domain.model.Notice;
import com.backend.immilog.notice.domain.model.NoticeDetail;
import com.backend.immilog.global.enums.Country;
import com.backend.immilog.notice.domain.model.NoticeStatus;
import com.backend.immilog.notice.domain.model.NoticeType;
import com.backend.immilog.notice.infrastructure.jdbc.NoticeJdbcRepository;
import com.backend.immilog.notice.infrastructure.jpa.NoticeJpaEntity;
import com.backend.immilog.notice.infrastructure.jpa.NoticeJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("NoticeRepositoryImpl 클래스 테스트")
class NoticeRepositoryImplTest {

    private final NoticeJdbcRepository noticeJdbcRepository = mock(NoticeJdbcRepository.class);
    private final NoticeJpaRepository noticeJpaRepository = mock(NoticeJpaRepository.class);
    private final NoticeRepositoryImpl noticeRepository = new NoticeRepositoryImpl(
            noticeJdbcRepository,
            noticeJpaRepository
    );

    @Test
    @DisplayName("공지사항 저장 - 성공")
    void save_savesNoticeSuccessfully() {
        Notice notice = new Notice(
                1L,
                1L,
                List.of(Country.SOUTH_KOREA),
                List.of(1L),
                LocalDateTime.now(),
                NoticeDetail.of("title", "content", NoticeType.NOTICE, NoticeStatus.NORMAL),
                LocalDateTime.now()
        );
        NoticeJpaEntity noticeJpaEntity = NoticeJpaEntity.from(notice);
        when(noticeJpaRepository.save(any(NoticeJpaEntity.class))).thenReturn(noticeJpaEntity);
        noticeRepository.save(notice);
        verify(noticeJpaRepository, times(1)).save(any(NoticeJpaEntity.class));
    }

    @Test
    @DisplayName("공지사항 조회 - 공지사항이 없는 경우")
    void getNotices_returnsEmptyPageWhenNoNotices() {
        Pageable pageable = PageRequest.of(0, 10);
        Long userSeq = 999L;
        when(noticeJdbcRepository.getNotices(userSeq, pageable.getPageSize(), pageable.getOffset())).thenReturn(List.of());
        when(noticeJdbcRepository.getTotal(userSeq)).thenReturn(0L);

        Page<NoticeResult> result = noticeRepository.getNotices(userSeq, pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isEqualTo(0L);
    }

    @Test
    @DisplayName("공지사항 존재유무 체크 - 성공")
    void findByGetSeq_returnsNoticeWhenExists() {
        Long noticeSeq = 1L;
        Notice notice = new Notice(
                1L,
                1L,
                List.of(Country.SOUTH_KOREA),
                List.of(1L),
                LocalDateTime.now(),
                NoticeDetail.of("title", "content", NoticeType.NOTICE, NoticeStatus.NORMAL),
                LocalDateTime.now()
        );
        NoticeJpaEntity noticeJpaEntity = NoticeJpaEntity.from(notice);
        when(noticeJpaRepository.findBySeqAndStatusIsNot(noticeSeq,NoticeStatus.DELETED)).thenReturn(Optional.of(noticeJpaEntity));

        Optional<Notice> result = noticeRepository.findBySeq(noticeSeq);

        assertThat(result).isPresent();
        assertThat(result.get().seq()).isEqualTo(noticeSeq);
    }

    @Test
    @DisplayName("공지사항 조회 - 공지사항이 없는 경우")
    void findByGetSeq_returnsEmptyWhenNotExists() {
        Long noticeSeq = 999L;
        when(noticeJpaRepository.findById(noticeSeq)).thenReturn(Optional.empty());

        Optional<Notice> result = noticeRepository.findBySeq(noticeSeq);

        assertThat(result).isNotPresent();
    }

    @Test
    @DisplayName("읽지 않은 공지사항 존재유무 체크 - 성공")
    void areUnreadNoticesExist_returnsTrueWhenUnreadNoticesExist() {
        Country country = Country.MALAYSIA;
        Long seq = 1L;
        when(noticeJpaRepository.existsByTargetCountryContainingAndReadUsersNotContaining(country, seq)).thenReturn(true);

        Boolean result = noticeRepository.areUnreadNoticesExist(country, seq);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("읽지 않은 공지사항 존재유무 체크 - 읽지 않은 공지사항이 없는 경우")
    void areUnreadNoticesExist_returnsFalseWhenNoUnreadNoticesExist() {
        Country country = Country.MALAYSIA;
        Long seq = 999L;
        when(noticeJpaRepository.existsByTargetCountryContainingAndReadUsersNotContaining(country, seq)).thenReturn(false);

        Boolean result = noticeRepository.areUnreadNoticesExist(country, seq);

        assertThat(result).isFalse();
    }
}