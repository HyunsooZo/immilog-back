package com.backend.immilog.notice.application.services.command;

import com.backend.immilog.notice.domain.model.Notice;
import com.backend.immilog.notice.domain.repositories.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoticeCommandService {
    private final NoticeRepository noticeRepository;

    @Transactional
    public void save(Notice notice) {
        noticeRepository.save(notice);
    }
}
