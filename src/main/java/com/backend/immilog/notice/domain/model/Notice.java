package com.backend.immilog.notice.domain.model;

import com.backend.immilog.notice.application.command.NoticeUploadCommand;
import com.backend.immilog.notice.domain.model.enums.NoticeCountry;
import com.backend.immilog.notice.domain.model.enums.NoticeStatus;
import com.backend.immilog.notice.domain.model.enums.NoticeType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Getter
public class Notice {
    private final Long seq;
    private final Long userSeq;
    private final List<NoticeCountry> targetCountries;
    private final List<Long> readUsers;
    private final LocalDateTime createdAt;
    private NoticeDetail detail;
    private LocalDateTime updatedAt;

    @Builder
    public Notice(
            Long seq,
            Long userSeq,
            NoticeDetail detail,
            List<NoticeCountry> targetCountries,
            List<Long> readUsers,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.seq = seq;
        this.userSeq = userSeq;
        this.detail = detail;
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
                .detail(NoticeDetail.of(
                        command.title(),
                        command.content(),
                        command.type(),
                        NoticeStatus.NORMAL
                ))
                .userSeq(userSeq)
                .targetCountries(command.targetCountries())
                .build();
    }

    public void updateTitle(String title) {
        Stream.of(this.detail)
                .filter(detail -> !this.detail.title().equals(title))
                .findFirst()
                .ifPresent(detail -> {
                            this.detail = NoticeDetail.of(
                                    title,
                                    this.detail.content(),
                                    this.detail.type(),
                                    this.detail.status()
                            );
                            updateUpdatedAt();
                        }
                );
    }

    public void updateContent(String content) {
        Stream.of(this.detail)
                .filter(detail -> !this.detail.content().equals(content))
                .findFirst()
                .ifPresent(detail -> {
                            this.detail = NoticeDetail.of(
                                    this.detail.title(),
                                    content,
                                    this.detail.type(),
                                    this.detail.status()
                            );
                            updateUpdatedAt();
                        }
                );
    }

    public void updateType(NoticeType type) {
        Stream.of(this.detail)
                .filter(detail -> !this.detail.type().equals(type))
                .findFirst()
                .ifPresent(detail -> {
                            this.detail = NoticeDetail.of(
                                    this.detail.title(),
                                    this.detail.content(),
                                    type,
                                    this.detail.status()
                            );
                            updateUpdatedAt();
                        }
                );
    }

    public void updateStatus(NoticeStatus status) {
        Stream.of(this.detail)
                .filter(detail -> !this.detail.status().equals(status))
                .findFirst()
                .ifPresent(detail -> {
                            this.detail = NoticeDetail.of(
                                    this.detail.title(),
                                    this.detail.content(),
                                    this.detail.type(),
                                    status
                            );
                            updateUpdatedAt();
                        }
                );
    }

    public void readByUser(Long userSeq) {
        this.readUsers.add(userSeq);
    }

    void updateUpdatedAt() {
        this.updatedAt = LocalDateTime.now();
    }

    public String getTitle() {
        return this.detail.title();
    }

    public String getContent() {
        return this.detail.content();
    }

    public NoticeType getType() {
        return this.detail.type();
    }

    public NoticeStatus getStatus() {
        return this.detail.status();
    }
}


