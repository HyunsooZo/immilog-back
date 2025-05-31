package com.backend.immilog.notice.application.usecase;

import com.backend.immilog.notice.application.dto.NoticeUploadCommand;
import com.backend.immilog.notice.application.services.NoticeCommandService;
import com.backend.immilog.notice.domain.Notice;
import com.backend.immilog.notice.domain.NoticeAuthPolicy;
import org.springframework.stereotype.Service;

public interface NoticeCreateUseCase {
    void createNotice(
            String token,
            NoticeUploadCommand command
    );

    @Service
    class NoticeCreator implements NoticeCreateUseCase {
        private final NoticeCommandService noticeCommandService;
        private final NoticeAuthPolicy noticeAuthPolicy;

        public NoticeCreator(
                NoticeCommandService noticeCommandService,
                NoticeAuthPolicy noticeAuthPolicy
        ) {
            this.noticeCommandService = noticeCommandService;
            this.noticeAuthPolicy = noticeAuthPolicy;
        }

        @Override
        public void createNotice(
                String token,
                NoticeUploadCommand command
        ) {
            noticeAuthPolicy.validateAdmin(token);
            var notice = Notice.of(
                    noticeAuthPolicy.getUserSeqFromToken(token),
                    command.title(),
                    command.content(),
                    command.type(),
                    command.targetCountry()
            );
            noticeCommandService.save(notice);
        }
    }
}
