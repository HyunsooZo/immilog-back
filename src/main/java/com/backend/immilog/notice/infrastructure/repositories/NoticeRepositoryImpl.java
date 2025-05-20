package com.backend.immilog.notice.infrastructure.repositories;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.notice.application.dto.NoticeResult;
import com.backend.immilog.notice.domain.model.Notice;
import com.backend.immilog.notice.domain.model.NoticeStatus;
import com.backend.immilog.notice.domain.repositories.NoticeRepository;
import com.backend.immilog.notice.infrastructure.jdbc.NoticeJdbcRepository;
import com.backend.immilog.notice.infrastructure.jpa.NoticeJpaEntity;
import com.backend.immilog.notice.infrastructure.jpa.NoticeJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class NoticeRepositoryImpl implements NoticeRepository {
    private final NoticeJdbcRepository noticeJdbcRepository;
    private final NoticeJpaRepository noticeJpaRepository;

    public NoticeRepositoryImpl(
            NoticeJdbcRepository noticeJdbcRepository,
            NoticeJpaRepository noticeJpaRepository
    ) {
        this.noticeJdbcRepository = noticeJdbcRepository;
        this.noticeJpaRepository = noticeJpaRepository;
    }

    @Override
    public Page<NoticeResult> getNotices(
            Long userSeq,
            Pageable pageable
    ) {
        List<NoticeResult> result = noticeJdbcRepository.getNotices(
                userSeq,
                pageable.getPageSize(),
                pageable.getOffset()
        );
        Long total = noticeJdbcRepository.getTotal(userSeq);
        return new PageImpl<>(result, pageable, total);
    }

    @Override
    public void save(Notice notice) {
        noticeJpaRepository.save(NoticeJpaEntity.from(notice));
    }

    @Override
    public Optional<Notice> findBySeq(Long noticeSeq) {
        return noticeJpaRepository
                .findBySeqAndStatusIsNot(noticeSeq, NoticeStatus.DELETED)
                .map(NoticeJpaEntity::toDomain);
    }

    @Override
    public Boolean areUnreadNoticesExist(
            Country country,
            Long seq
    ) {
        return noticeJpaRepository.existsByTargetCountryContainingAndReadUsersNotContaining(country, seq);
    }
}
