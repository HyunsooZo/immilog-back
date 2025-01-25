package com.backend.immilog.post.domain.model.comment;

import com.backend.immilog.post.domain.enums.PostStatus;
import com.backend.immilog.post.domain.enums.ReferenceType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class Comment {
    private final Long seq;
    private final Long userSeq;
    private final String content;
    private final CommentRelation commentRelation;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private int replyCount;
    private Integer likeCount;
    private PostStatus status;
    private List<Long> likeUsers;

    @Builder
    public Comment(
            Long seq,
            Long userSeq,
            int replyCount,
            Integer likeCount,
            String content,
            CommentRelation commentRelation,
            PostStatus status,
            List<Long> likeUsers,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.seq = seq;
        this.userSeq = userSeq;
        this.replyCount = replyCount;
        this.likeCount = likeCount;
        this.content = content;
        this.commentRelation = commentRelation;
        this.status = status;
        this.likeUsers = likeUsers;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Comment of(
            Long userSeq,
            Long postSeq,
            String content,
            ReferenceType referenceType
    ) {
        return Comment.builder()
                .userSeq(userSeq)
                .content(content)
                .likeCount(0)
                .replyCount(0)
                .status(PostStatus.NORMAL)
                .commentRelation(CommentRelation.of(postSeq, null, referenceType))
                .likeUsers(new ArrayList<>())
                .build();
    }

    public void delete() {
        this.status = PostStatus.DELETED;
    }

    public void addLikeUser(Long userSeq) {
        if (Objects.isNull(this.likeUsers)) {
            this.likeUsers = new ArrayList<>();
        }
        this.likeUsers.add(userSeq);
        increaseLikeCount();
    }

    public Long getPostSeq() {
        return this.commentRelation.getPostSeq();
    }

    public Long getParentSeq() {
        return this.commentRelation.getParentSeq();
    }

    public ReferenceType getReferenceType() {
        return this.commentRelation.getReferenceType();
    }

    public void increaseReplyCount() {
        this.replyCount++;
    }

    private void increaseLikeCount() {
        this.likeCount++;
    }

}

