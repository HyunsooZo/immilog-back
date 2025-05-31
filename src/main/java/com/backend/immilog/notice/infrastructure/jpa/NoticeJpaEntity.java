package com.backend.immilog.notice.infrastructure.jpa;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.notice.domain.Notice;
import com.backend.immilog.notice.domain.NoticeDetail;
import com.backend.immilog.notice.domain.NoticeStatus;
import com.backend.immilog.notice.domain.NoticeType;
import jakarta.persistence.*;
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
public class NoticeJpaEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "seq")
    private Long seq;

    @Column(name = "user_seq")
    private Long userSeq;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private NoticeType type;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private NoticeStatus status;

    @ElementCollection(fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Country> targetCountry;

    @ElementCollection(fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Long> readUsers;

    @CreatedDate
    @Column(name = "created_at")
    private final LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected NoticeJpaEntity() {}

    protected NoticeJpaEntity(
            Long seq,
            Long userSeq,
            String title,
            String content,
            NoticeType type,
            NoticeStatus status,
            List<Country> targetCountry,
            List<Long> readUsers
    ) {
        this.seq = seq;
        this.userSeq = userSeq;
        this.title = title;
        this.content = content;
        this.type = type;
        this.status = status;
        this.targetCountry = targetCountry;
        this.readUsers = readUsers;
        this.updatedAt = seq == null ? null : LocalDateTime.now();
    }

    public static NoticeJpaEntity from(Notice notice) {
        return new NoticeJpaEntity(
                notice.seq(),
                notice.userSeq(),
                notice.title(),
                notice.content(),
                notice.type(),
                notice.status(),
                notice.targetCountry(),
                notice.readUsers()
        );
    }

    public Notice toDomain() {
        return new Notice(
                this.seq,
                this.userSeq,
                this.targetCountry,
                this.readUsers,
                this.createdAt,
                NoticeDetail.of(this.title, this.content, this.type, this.status),
                this.updatedAt
        );
    }
}

