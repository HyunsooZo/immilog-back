package com.backend.immilog.notice.application.services;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.notice.application.result.NoticeResult;
import com.backend.immilog.notice.application.services.query.NoticeQueryService;
import com.backend.immilog.user.application.services.query.UserQueryService;
import com.backend.immilog.user.domain.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class NoticeInquiryService {
    private final NoticeQueryService noticeQueryService;
    private final UserQueryService userQueryService;

    public NoticeInquiryService(
            NoticeQueryService noticeQueryService,
            UserQueryService userQueryService
    ) {
        this.noticeQueryService = noticeQueryService;
        this.userQueryService = userQueryService;
    }

    public Page<NoticeResult> getNotices(
            Long userSeq,
            Integer page
    ) {
        if (userSeq == null) {
            return Page.empty();
        }
        Pageable pageable = PageRequest.of(page, 10);
        return noticeQueryService.getNotices(userSeq, pageable);
    }

    public NoticeResult getNoticeDetail(Long noticeSeq) {
        return NoticeResult.from(noticeQueryService.getNoticeBySeq(noticeSeq));
    }

    public Boolean isUnreadNoticeExist(Long userSeq) {
        User user = userQueryService.getUserById(userSeq);
        return noticeQueryService.areUnreadNoticesExist(
                Country.valueOf(user.countryName()),
                user.seq()
        );
    }
}
