package com.backend.immilog.notice.domain;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.notice.application.dto.NoticeModelResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface NoticeRepository {
    Page<NoticeModelResult> getNotices(
            Long userSeq,
            Pageable pageable
    );

    void save(Notice notice);

    Optional<Notice> findBySeq(Long noticeSeq);

    Boolean areUnreadNoticesExist(
            Country country,
            Long seq
    );
}
