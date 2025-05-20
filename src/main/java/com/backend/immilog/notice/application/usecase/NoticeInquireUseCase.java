package com.backend.immilog.notice.application.usecase;

import com.backend.immilog.notice.application.dto.NoticeResult;
import org.springframework.data.domain.Page;

public interface NoticeInquireUseCase {
    Page<NoticeResult> getNotices(
            Long userSeq,
            Integer page
    );

    NoticeResult getNoticeDetail(Long noticeSeq);

    Boolean isUnreadNoticeExist(Long userSeq);
}
