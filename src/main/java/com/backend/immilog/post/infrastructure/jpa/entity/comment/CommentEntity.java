package com.backend.immilog.post.infrastructure.jpa.entity.comment;

import com.backend.immilog.post.domain.enums.PostStatus;
import com.backend.immilog.post.domain.enums.ReferenceType;
import com.backend.immilog.post.domain.model.comment.Comment;
import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.List;

@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "comment")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;

    @Column(name = "user_seq")
    private Long userSeq;

    @Column(name = "reply_count")
    private int replyCount;

    @Column(name = "like_count")
    private Integer likeCount;

    @Column(name = "content")
    private String content;

    @Embedded
    private CommentRelationValue sourceData;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PostStatus status;

    @ElementCollection(fetch = FetchType.LAZY)
    @Cascade(value = CascadeType.ALL)
    private List<Long> likeUsers;

    @Column(name = "created_at")
    private final LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected CommentEntity() {}

    protected CommentEntity(
            Long seq,
            Long userSeq,
            Long postSeq,
            Long parentSeq,
            int replyCount,
            Integer likeCount,
            String content,
            ReferenceType referenceType,
            PostStatus status,
            List<Long> likeUsers,
            LocalDateTime updatedAt
    ) {
        CommentRelationValue sourceData = CommentRelationValue.of(
                postSeq,
                parentSeq,
                referenceType
        );
        this.seq = seq;
        this.userSeq = userSeq;
        this.replyCount = replyCount;
        this.likeCount = likeCount;
        this.content = content;
        this.sourceData = sourceData;
        this.status = status;
        this.likeUsers = likeUsers;
        this.updatedAt = updatedAt;
    }

    public static CommentEntity from(Comment comment) {
        return new CommentEntity(
                comment.seq(),
                comment.userSeq(),
                comment.postSeq(),
                comment.parentSeq(),
                comment.replyCount(),
                comment.likeCount(),
                comment.content(),
                comment.referenceType(),
                comment.status(),
                comment.likeUsers(),
                comment.seq() != null ? LocalDateTime.now() : null
        );
    }

    public Comment toDomain() {
        return new Comment(
                this.seq,
                this.userSeq,
                this.content,
                this.sourceData.toDomain(),
                this.replyCount,
                this.likeCount,
                this.status,
                this.likeUsers,
                this.createdAt,
                this.updatedAt
        );
    }
}

