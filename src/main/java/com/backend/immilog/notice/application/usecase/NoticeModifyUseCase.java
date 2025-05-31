package com.backend.immilog.notice.application.usecase;

import com.backend.immilog.notice.application.dto.NoticeModifyCommand;
import com.backend.immilog.notice.application.services.NoticeCommandService;
import com.backend.immilog.notice.application.services.NoticeQueryService;
import com.backend.immilog.notice.domain.NoticeAuthPolicy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

public interface NoticeModifyUseCase {
    void modifyNotice(
            String token,
            Long noticeSeq,
            NoticeModifyCommand command
    );

    void readNotice(
            Long userSeq,
            Long noticeSeq
    );

    @Service
    class NoticeModifier implements NoticeModifyUseCase {
        private final NoticeQueryService noticeQueryService;
        private final NoticeCommandService noticeCommandService;
        private final NoticeAuthPolicy noticeAuthPolicy;

        public NoticeModifier(
                NoticeQueryService noticeQueryService,
                NoticeCommandService noticeCommandService,
                NoticeAuthPolicy noticeAuthPolicy
        ) {
            this.noticeQueryService = noticeQueryService;
            this.noticeCommandService = noticeCommandService;
            this.noticeAuthPolicy = noticeAuthPolicy;
        }

        @Transactional
        public void modifyNotice(
                String token,
                Long noticeSeq,
                NoticeModifyCommand command
        ) {
            noticeAuthPolicy.validateAdmin(token);
            var notice = noticeQueryService.getNoticeBySeq(noticeSeq);
            var updatedNotice = notice.updateTitle(command.title())
                    .updateContent(command.content())
                    .updateType(command.type())
                    .updateStatus(command.status());
            noticeCommandService.save(updatedNotice);
        }

        @Transactional
        public void readNotice(
                Long userSeq,
                Long noticeSeq
        ) {
            var notice = noticeQueryService.getNoticeBySeq(noticeSeq);
            var updatedNotice = notice.readByUser(userSeq);
            noticeCommandService.save(updatedNotice);
        }
    }
}

