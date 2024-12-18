package com.backend.immilog.notice.infrastructure.jpa;

import com.backend.immilog.notice.domain.model.Notice;
import com.backend.immilog.notice.domain.model.enums.NoticeCountry;
import com.backend.immilog.notice.domain.model.enums.NoticeStatus;
import com.backend.immilog.notice.domain.model.enums.NoticeType;
import jakarta.persistence.*;
import lombok.Builder;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@DynamicUpdate
@Entity
@Table(name = "notice")
public class NoticeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long seq;

    private Long userSeq;

    private String title;

    private String content;

    private NoticeType type;

    private NoticeStatus status;

    @ElementCollection(fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<NoticeCountry> targetCountries;

    @ElementCollection(fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Long> readUsers;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    protected NoticeEntity() {}

    @Builder
    NoticeEntity(
            Long seq,
            Long userSeq,
            String title,
            String content,
            NoticeType type,
            NoticeStatus status,
            List<NoticeCountry> targetCountries,
            List<Long> readUsers
    ) {
        this.seq = seq;
        this.userSeq = userSeq;
        this.title = title;
        this.content = content;
        this.type = type;
        this.status = status;
        this.targetCountries = targetCountries;
        this.readUsers = readUsers;
    }

    public static NoticeEntity from(
            Notice notice
    ) {
        return NoticeEntity.builder()
                .seq(notice.getSeq())
                .userSeq(notice.getUserSeq())
                .title(notice.getTitle())
                .content(notice.getContent())
                .type(notice.getType())
                .status(notice.getStatus())
                .targetCountries(notice.getTargetCountries())
                .readUsers(notice.getReadUsers())
                .build();
    }

    public Notice toDomain(
    ) {
        return Notice.builder()
                .seq(this.seq)
                .userSeq(this.userSeq)
                .title(this.title)
                .content(this.content)
                .type(this.type)
                .status(this.status)
                .targetCountries(this.targetCountries)
                .readUsers(this.readUsers)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}

