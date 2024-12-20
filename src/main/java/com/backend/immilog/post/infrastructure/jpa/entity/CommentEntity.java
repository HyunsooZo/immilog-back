package com.backend.immilog.post.infrastructure.jpa.entity;

import com.backend.immilog.global.model.BaseDateEntity;
import com.backend.immilog.post.domain.enums.PostStatus;
import com.backend.immilog.post.domain.enums.ReferenceType;
import com.backend.immilog.post.domain.model.comment.Comment;
import jakarta.persistence.*;
import lombok.Builder;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

@DynamicUpdate
@Entity
@Table(name = "comment")
public class CommentEntity extends BaseDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private Long userSeq;

    private Long postSeq;

    private Long parentSeq;

    private int replyCount;

    private Integer likeCount;

    private String content;

    @Enumerated(EnumType.STRING)
    private ReferenceType referenceType;

    @Enumerated(EnumType.STRING)
    private PostStatus status;

    @ElementCollection(fetch = FetchType.LAZY)
    @Cascade(value = CascadeType.ALL)
    private List<Long> likeUsers;

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
            List<Long> likeUsers
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
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .build();
    }

}

