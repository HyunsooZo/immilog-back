package com.backend.immilog.notice.infrastructure.jpa;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.notice.domain.NoticeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoticeJpaRepository extends JpaRepository<NoticeJpaEntity, Long> {
    Boolean existsByTargetCountryContainingAndReadUsersNotContaining(
            Country country,
            Long seq
    );

    Optional<NoticeJpaEntity> findBySeqAndStatusIsNot(
            Long noticeSeq,
            NoticeStatus status
    );
}
