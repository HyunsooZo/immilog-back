package com.backend.immilog.notice.infrastructure.jpa;

import com.backend.immilog.notice.domain.model.Notice;
import com.backend.immilog.notice.domain.model.enums.NoticeCountry;
import com.backend.immilog.notice.domain.model.enums.NoticeStatus;
import com.backend.immilog.notice.domain.model.enums.NoticeType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@DynamicUpdate
@Entity
@Table(name = "notice")
public class NoticeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long seq;

    private Long userSeq;

    @Setter
    private String title;

    @Setter
    private String content;

    @Setter
    private NoticeType type;

    @Setter
    private NoticeStatus status;

    @ElementCollection(fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<NoticeCountry> targetCountries;

    @ElementCollection(fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Long> readUsers;

    @Setter
    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

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
                .seq(this.getSeq())
                .userSeq(this.getUserSeq())
                .title(this.getTitle())
                .content(this.getContent())
                .type(this.getType())
                .status(this.getStatus())
                .targetCountries(this.getTargetCountries())
                .readUsers(this.getReadUsers())
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .build();
    }
}

