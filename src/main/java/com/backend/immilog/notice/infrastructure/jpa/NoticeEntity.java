package com.backend.immilog.notice.infrastructure.jpa;

import com.backend.immilog.notice.domain.model.Notice;
import com.backend.immilog.notice.domain.model.NoticeDetail;
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
    private List<NoticeCountry> targetCountries;

    @ElementCollection(fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Long> readUsers;

    @CreatedDate
    @Column(name = "created_at")
    private final LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected NoticeEntity() {}

    @Builder
    protected NoticeEntity(
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
        this.updatedAt = seq == null ? null : LocalDateTime.now();
    }

    public static NoticeEntity from(Notice notice) {
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

    public Notice toDomain() {
        return Notice.builder()
                .seq(this.seq)
                .userSeq(this.userSeq)
                .detail(NoticeDetail.of(this.title, this.content, this.type, this.status))
                .targetCountries(this.targetCountries)
                .readUsers(this.readUsers)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}

