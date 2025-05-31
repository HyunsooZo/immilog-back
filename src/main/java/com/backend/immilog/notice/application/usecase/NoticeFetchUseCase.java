package com.backend.immilog.notice.application.usecase;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.notice.application.dto.NoticeModelResult;
import com.backend.immilog.notice.application.services.NoticeQueryService;
import com.backend.immilog.user.application.services.UserQueryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

public interface NoticeFetchUseCase {
    Page<NoticeModelResult> getNotices(
            Long userSeq,
            Integer page
    );

    NoticeModelResult getNoticeDetail(Long noticeSeq);

    Boolean isUnreadNoticeExist(Long userSeq);

    @Service
    class NoticeFetcher implements NoticeFetchUseCase {
        private final NoticeQueryService noticeQueryService;
        private final UserQueryService userQueryService;

        public NoticeFetcher(
                NoticeQueryService noticeQueryService,
                UserQueryService userQueryService
        ) {
            this.noticeQueryService = noticeQueryService;
            this.userQueryService = userQueryService;
        }

        @Override
        public Page<NoticeModelResult> getNotices(
                Long userSeq,
                Integer page
        ) {
            if (userSeq == null) {
                return Page.empty();
            }
            var pageable = PageRequest.of(page, 10);
            return noticeQueryService.getNotices(userSeq, pageable);
        }

        @Override
        public NoticeModelResult getNoticeDetail(Long noticeSeq) {
            return NoticeModelResult.from(noticeQueryService.getNoticeBySeq(noticeSeq));
        }

        @Override
        public Boolean isUnreadNoticeExist(Long userSeq) {
            var user = userQueryService.getUserById(userSeq);
            return noticeQueryService.areUnreadNoticesExist(Country.valueOf(user.countryName()), user.seq());
        }
    }
}
