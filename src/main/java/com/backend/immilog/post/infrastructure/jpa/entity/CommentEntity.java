package com.backend.immilog.post.infrastructure.jpa.entity;

import com.backend.immilog.post.domain.enums.PostStatus;
import com.backend.immilog.post.domain.enums.ReferenceType;
import com.backend.immilog.post.domain.model.comment.Comment;
import jakarta.persistence.*;
import lombok.Builder;
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

    @Column(name = "post_seq")
    private Long postSeq;

    @Column(name = "parent_seq")
    private Long parentSeq;

    @Column(name = "reply_count")
    private int replyCount;

    @Column(name = "like_count")
    private Integer likeCount;

    @Column(name = "content")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "reference_type")
    private ReferenceType referenceType;

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

    @Builder
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
        this.seq = seq;
        this.userSeq = userSeq;
        this.postSeq = postSeq;
        this.parentSeq = parentSeq;
        this.replyCount = replyCount;
        this.likeCount = likeCount;
        this.content = content;
        this.referenceType = referenceType;
        this.status = status;
        this.likeUsers = likeUsers;
        this.updatedAt = updatedAt;
    }

    public static CommentEntity from(Comment comment) {
        return CommentEntity.builder()
                .seq(comment.getSeq())
                .userSeq(comment.getUserSeq())
                .postSeq(comment.getPostSeq())
                .parentSeq(comment.getParentSeq())
                .replyCount(comment.getReplyCount())
                .likeCount(comment.getLikeCount())
                .content(comment.getContent())
                .referenceType(comment.getReferenceType())
                .status(comment.getStatus())
                .likeUsers(comment.getLikeUsers())
                .updatedAt(comment.getSeq() != null ? LocalDateTime.now() : null)
                .build();
    }

    public Comment toDomain() {
        return Comment.builder()
                .seq(this.seq)
                .userSeq(this.userSeq)
                .postSeq(this.postSeq)
                .parentSeq(this.parentSeq)
                .replyCount(this.replyCount)
                .likeCount(this.likeCount)
                .content(this.content)
                .referenceType(this.referenceType)
                .status(this.status)
                .likeUsers(this.likeUsers)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }

}

