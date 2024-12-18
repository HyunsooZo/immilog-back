package com.backend.immilog.notice.application.services.command;

import com.backend.immilog.notice.domain.model.Notice;
import com.backend.immilog.notice.domain.repositories.NoticeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NoticeCommandService {
    private final NoticeRepository noticeRepository;

    public NoticeCommandService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    @Transactional
    public void save(Notice notice) {
        noticeRepository.save(notice);
    }
}
