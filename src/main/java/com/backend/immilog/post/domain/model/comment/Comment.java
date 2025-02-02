package com.backend.immilog.post.domain.model.comment;

import com.backend.immilog.post.domain.enums.PostStatus;
import com.backend.immilog.post.domain.enums.ReferenceType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record Comment(
        Long seq,
        Long userSeq,
        String content,
        CommentRelation commentRelation,
        int replyCount,
        Integer likeCount,
        PostStatus status,
        List<Long> likeUsers,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static Comment of(
            Long userSeq,
            Long postSeq,
            String content,
            ReferenceType referenceType
    ) {
        return new Comment(
                null,
                userSeq,
                content,
                CommentRelation.of(postSeq, null, referenceType),
                0,
                0,
                PostStatus.NORMAL,
                new ArrayList<>(),
                LocalDateTime.now(),
                null
        );
    }

    public Comment delete() {
        return new Comment(
                this.seq,
                this.userSeq,
                this.content,
                this.commentRelation,
                this.replyCount,
                this.likeCount,
                PostStatus.DELETED,
                this.likeUsers,
                this.createdAt,
                LocalDateTime.now()
        );
    }

    public Comment addLikeUser(Long userSeq) {
        if (!Objects.isNull(this.likeUsers)) {
            List<Long> newLikeUsers = this.likeUsers;
            newLikeUsers.add(userSeq);
            Integer newLikeCount = this.likeCount + 1;
            return new Comment(
                    this.seq,
                    this.userSeq,
                    this.content,
                    this.commentRelation,
                    this.replyCount,
                    newLikeCount,
                    this.status,
                    newLikeUsers,
                    this.createdAt,
                    this.updatedAt
            );
        }
        return this;
    }

    public Comment increaseReplyCount() {
        return new Comment(
                this.seq,
                this.userSeq,
                this.content,
                this.commentRelation,
                this.replyCount + 1,
                this.likeCount,
                this.status,
                this.likeUsers,
                this.createdAt,
                this.updatedAt
        );
    }

    public Long postSeq() {
        return this.commentRelation.postSeq();
    }

    public Long parentSeq() {
        return this.commentRelation.parentSeq();
    }

    public ReferenceType referenceType() {
        return this.commentRelation.referenceType();
    }

}

