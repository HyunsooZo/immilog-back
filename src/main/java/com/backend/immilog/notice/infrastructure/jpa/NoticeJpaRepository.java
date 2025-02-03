package com.backend.immilog.notice.infrastructure.jpa;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.notice.domain.model.enums.NoticeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoticeJpaRepository extends JpaRepository<NoticeEntity, Long> {
    Boolean existsByTargetCountryContainingAndReadUsersNotContaining(
            Country country,
            Long seq
    );

    Optional<NoticeEntity> findBySeqAndStatusIsNot(
            Long noticeSeq,
            NoticeStatus status
    );
}
