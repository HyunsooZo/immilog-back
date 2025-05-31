package com.backend.immilog.notice.domain;

import com.backend.immilog.global.enums.Country;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Notice {
    private final Long seq;
    private final Long userSeq;
    private final List<Country> targetCountry;
    private final List<Long> readUsers;
    private final LocalDateTime createdAt;
    private NoticeDetail detail;
    private final LocalDateTime updatedAt;

    public Notice(
            Long seq,
            Long userSeq,
            List<Country> targetCountry,
            List<Long> readUsers,
            LocalDateTime createdAt,
            NoticeDetail detail,
            LocalDateTime updatedAt
    ) {
        this.seq = seq;
        this.userSeq = userSeq;
        this.targetCountry = targetCountry;
        this.readUsers = readUsers;
        this.createdAt = createdAt;
        this.detail = detail;
        this.updatedAt = updatedAt;
    }

    public static Notice of(
            Long userSeq,
            String title,
            String content,
            NoticeType type,
            List<Country> targetCountry
    ) {
        return new Notice(
                null,
                userSeq,
                targetCountry,
                new ArrayList<>(),
                LocalDateTime.now(),
                NoticeDetail.of(title, content, type, NoticeStatus.NORMAL),
                LocalDateTime.now()
        );
    }

    public Notice updateTitle(String title) {
        if (title == null || this.detail.title().equals(title)) {
            return this;
        }
        return new Notice(
                this.seq,
                this.userSeq,
                this.targetCountry,
                this.readUsers,
                this.createdAt,
                NoticeDetail.of(title, this.detail.content(), this.detail.type(), this.detail.status()),
                LocalDateTime.now()
        );
    }

    public Notice updateContent(String content) {
        if (content == null || this.detail.content().equals(content)) {
            return this;
        }
        this.detail = NoticeDetail.of(this.detail.title(), content, this.detail.type(), this.detail.status());
        return this;
    }

    public Notice updateType(NoticeType type) {
        if (type == null || this.detail.type().equals(type)) {
            return this;
        }
        this.detail = NoticeDetail.of(this.detail.title(), this.detail.content(), type, this.detail.status());
        return this;
    }

    public Notice updateStatus(NoticeStatus status) {
        if (status == null || this.detail.status().equals(status)) {
            return this;
        }
        this.detail = NoticeDetail.of(this.detail.title(), this.detail.content(), this.detail.type(), status);
        return this;
    }

    public Notice readByUser(Long userSeq) {
        if (userSeq == null || this.readUsers.contains(userSeq)) {
            return this;
        }
        this.readUsers.add(userSeq);
        return this;
    }

    public String title() {
        return this.detail.title();
    }

    public String content() {
        return this.detail.content();
    }

    public NoticeType type() {
        return this.detail.type();
    }

    public NoticeStatus status() {
        return this.detail.status();
    }

    public Long seq() {return seq;}

    public Long userSeq() {return userSeq;}

    public List<Country> targetCountry() {return targetCountry;}

    public List<Long> readUsers() {return readUsers;}

    public LocalDateTime createdAt() {return createdAt;}

    public LocalDateTime updatedAt() {return updatedAt;}
}


