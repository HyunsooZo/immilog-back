package com.backend.immilog.notice.domain.repositories;

import com.backend.immilog.notice.application.result.NoticeResult;
import com.backend.immilog.notice.domain.model.Notice;
import com.backend.immilog.notice.domain.model.enums.NoticeCountry;
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
            NoticeCountry country,
            Long seq
    );

    Optional<Notice> getNotice(Long noticeSeq);
}
