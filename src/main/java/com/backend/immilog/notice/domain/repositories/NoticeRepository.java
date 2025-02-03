package com.backend.immilog.notice.domain.repositories;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.notice.application.result.NoticeResult;
import com.backend.immilog.notice.domain.model.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface NoticeRepository {
    Page<NoticeResult> getNotices(
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
