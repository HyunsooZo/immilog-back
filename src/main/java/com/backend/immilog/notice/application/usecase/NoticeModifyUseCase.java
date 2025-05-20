package com.backend.immilog.notice.application.usecase;

import com.backend.immilog.notice.application.dto.NoticeModifyCommand;

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
}

