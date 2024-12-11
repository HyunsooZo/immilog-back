package com.backend.immilog.notice.domain.model;

import com.backend.immilog.notice.application.command.NoticeUploadCommand;
import com.backend.immilog.notice.domain.model.enums.NoticeCountry;
import com.backend.immilog.notice.domain.model.enums.NoticeStatus;
import com.backend.immilog.notice.domain.model.enums.NoticeType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Getter
public class Notice {
    private final Long seq;
    private final Long userSeq;
    private String title;
    private String content;
    private NoticeType type;
    private NoticeStatus status;
    private final List<NoticeCountry> targetCountries;
    private final List<Long> readUsers;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public Notice(
            Long seq,
            Long userSeq,
            String title,
            String content,
            NoticeType type,
            NoticeStatus status,
            List<NoticeCountry> targetCountries,
            List<Long> readUsers,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.seq = seq;
        this.userSeq = userSeq;
        this.title = title;
        this.content = content;
        this.type = type;
        this.status = status;
        this.targetCountries = targetCountries;
        this.readUsers = readUsers;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Notice of(
            Long userSeq,
            NoticeUploadCommand command
    ) {
        return Notice.builder()
                .title(command.title())
                .userSeq(userSeq)
                .content(command.content())
                .type(command.type())
                .targetCountries(command.targetCountries())
                .status(NoticeStatus.NORMAL)
                .build();
    }

    public void updateTitle(String title) {
        Optional.ofNullable(title).ifPresent(newTitle -> {
            this.title = newTitle;
            updateUpdatedAt();
        });
    }

    public void updateContent(String content) {
        Optional.ofNullable(content).ifPresent(newContent -> {
            this.content = newContent;
            updateUpdatedAt();
        });
    }

    public void updateType(NoticeType type) {
        Optional.ofNullable(type).ifPresent(newType -> {
            this.type = newType;
            updateUpdatedAt();
        });
    }

    public void updateStatus(NoticeStatus status) {
        Optional.ofNullable(status).ifPresent(newStatus -> {
            this.status = newStatus;
            updateUpdatedAt();
        });
    }

    public void readByUser(Long userSeq) {
        updateReadUsers(userSeq);
    }

    void updateUpdatedAt() {
        this.updatedAt = LocalDateTime.now();
    }

    void updateReadUsers(Long userSeq) {
        this.readUsers.add(userSeq);
    }
}


