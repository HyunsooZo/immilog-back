package com.backend.immilog.notice.application.services.query;

import com.backend.immilog.notice.application.result.NoticeResult;
import com.backend.immilog.notice.domain.model.Notice;
import com.backend.immilog.notice.domain.model.enums.NoticeCountry;
import com.backend.immilog.notice.domain.repositories.NoticeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class NoticeQueryService {
    private final NoticeRepository noticeRepository;

    public NoticeQueryService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    @Transactional(readOnly = true)
    public Page<NoticeResult> getNotices(
            Long userSeq,
            Pageable pageable
    ) {
        return noticeRepository.getNotices(userSeq, pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Notice> getNoticeBySeq(Long noticeSeq) {
        return noticeRepository.findBySeq(noticeSeq);
    }

    @Transactional(readOnly = true)
    public Boolean areUnreadNoticesExist(
            NoticeCountry noticeCountry,
            Long seq
    ) {
        return noticeRepository.areUnreadNoticesExist(noticeCountry, seq);
    }
}
