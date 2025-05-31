package com.backend.immilog.post.domain.model.post;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.post.application.result.PostResult;
import com.backend.immilog.post.exception.PostErrorCode;
import com.backend.immilog.post.exception.PostException;
import com.backend.immilog.user.domain.model.user.User;

import java.time.LocalDateTime;

public class Post {
    private final Long seq;
    private final PostUserInfo postUserInfo;
    private PostInfo postInfo;
    private final Categories category;
    private String isPublic;
    private Badge badge;
    private Long commentCount;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Post(
            Long seq,
            PostUserInfo postUserInfo,
            PostInfo postInfo,
            Categories category,
            String isPublic,
            Badge badge,
            Long commentCount,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.seq = seq;
        this.postUserInfo = postUserInfo;
        this.postInfo = postInfo;
        this.category = category;
        this.isPublic = isPublic;
        this.badge = badge;
        this.commentCount = commentCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Post of(
            User user,
            String title,
            String content,
            Categories category,
            String isPublic
    ) {
        final var postInfo = PostInfo.of(title, content, user.country(), user.region());
        final var postUserInfo = new PostUserInfo(user.seq(), user.nickname(), user.imageUrl());
        return new Post(
                null,
                postUserInfo,
                postInfo,
                category,
                isPublic,
                null,
                0L,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public Post increaseCommentCount() {
        if (this.postInfo.status() == PostStatus.DELETED) {
            throw new PostException(PostErrorCode.POST_ALREADY_DELETED);
        }
        this.commentCount++;
        return this;
    }

    public Post updateIsPublic(Boolean isPublic) {
        if (isPublic == null) {
            throw new PostException(PostErrorCode.INVALID_PUBLIC_STATUS);
        }
        if (this.status().equals(PostStatus.DELETED)) {
            throw new PostException(PostErrorCode.POST_ALREADY_DELETED);
        }
        var value = isPublic ? "Y" : "N";
        if (this.isPublic.equals(value)) {
            return this;
        }
        this.isPublic = value;
        this.updatedAt = LocalDateTime.now();
        return this;
    }

    public Post updateContent(String newContent) {
        if (newContent == null || this.postInfo.content().equals(newContent)) {
            return this;
        }
        if (this.postInfo.status() == PostStatus.DELETED) {
            throw new PostException(PostErrorCode.POST_ALREADY_DELETED);
        }
        this.postInfo = new PostInfo(
                this.postInfo.title(),
                newContent,
                this.postInfo.viewCount(),
                this.postInfo.likeCount(),
                this.postInfo.region(),
                this.postInfo.status(),
                this.postInfo.country()
        );
        this.updatedAt = LocalDateTime.now();
        return this;
    }

    public Post updateTitle(String title) {
        if (title == null || this.postInfo.title().equals(title)) {
            return this;
        }
        if (this.postInfo.status() == PostStatus.DELETED) {
            throw new PostException(PostErrorCode.POST_ALREADY_DELETED);
        }
        this.postInfo = new PostInfo(
                title,
                this.postInfo.content(),
                this.postInfo.viewCount(),
                this.postInfo.likeCount(),
                this.postInfo.region(),
                this.postInfo.status(),
                this.postInfo.country()
        );
        this.updatedAt = LocalDateTime.now();
        return this;
    }

    public Post delete() {
        if (this.postInfo.status() == PostStatus.DELETED) {
            throw new PostException(PostErrorCode.POST_ALREADY_DELETED);
        }
        this.postInfo = new PostInfo(
                this.postInfo.title(),
                this.postInfo.content(),
                this.postInfo.viewCount(),
                this.postInfo.likeCount(),
                this.postInfo.region(),
                PostStatus.DELETED,
                this.postInfo.country()
        );
        this.updatedAt = LocalDateTime.now();
        return this;
    }

    public Long userSeq() {return this.postUserInfo.userSeq();}

    public Post increaseViewCount() {
        if (this.postInfo.status() == PostStatus.DELETED) {
            throw new PostException(PostErrorCode.POST_ALREADY_DELETED);
        }
        this.postInfo = new PostInfo(
                this.postInfo.title(),
                this.postInfo.content(),
                this.postInfo.viewCount() + 1,
                this.postInfo.likeCount(),
                this.postInfo.region(),
                this.postInfo.status(),
                this.postInfo.country()
        );
        return this;
    }

    public Post updateBadge(Badge badge) {
        if (this.badge != null && this.badge.equals(badge)) {
            return this;
        }
        this.badge = badge;
        return this;
    }

    public String title() {return this.postInfo.title();}

    public String content() {return this.postInfo.content();}

    public String countryName() {return this.postInfo.country().name();}

    public String region() {return this.postInfo.region();}

    public Long viewCount() {return this.postInfo.viewCount();}

    public Long likeCount() {return this.postInfo.likeCount();}

    public PostStatus status() {return this.postInfo.status();}

    public String profileImage() {return this.postUserInfo.profileImage();}

    public String nickname() {return this.postUserInfo.nickname();}

    public PostResult toResult() {
        return new PostResult(
                this.seq,
                this.postUserInfo.userSeq(),
                this.postUserInfo.profileImage(),
                this.postUserInfo.nickname(),
                this.commentCount,
                this.postInfo.viewCount(),
                this.postInfo.likeCount(),
                this.isPublic,
                this.countryName(),
                this.region(),
                this.category,
                this.postInfo.status(),
                this.title(),
                this.content(),
                this.createdAt.toString(),
                this.updatedAt == null ? null : this.updatedAt.toString()
        );
    }

    public static Post from(PostResult postResult) {
        final PostUserInfo userInfo = new PostUserInfo(
                postResult.getUserSeq(),
                postResult.getUserNickName(),
                postResult.getUserProfileUrl()
        );
        final PostInfo postInfo = new PostInfo(
                postResult.getTitle(),
                postResult.getContent(),
                postResult.getViewCount(),
                postResult.getLikeCount(),
                postResult.getRegion(),
                postResult.getStatus(),
                Country.valueOf(postResult.getCountry())
        );
        return new Post(
                postResult.getSeq(),
                userInfo,
                postInfo,
                postResult.getCategory(),
                postResult.getIsPublic(),
                null,
                postResult.getCommentCount(),
                LocalDateTime.parse(postResult.getCreatedAt()),
                LocalDateTime.parse(postResult.getUpdatedAt())
        );
    }

    public void validateUserId(Long userId) {
        if (!this.userSeq().equals(userId)) {
            throw new PostException(PostErrorCode.NO_AUTHORITY);
        }
    }

    public Long seq() {return seq;}

    public PostUserInfo postUserInfo() {return postUserInfo;}

    public PostInfo postInfo() {return postInfo;}

    public Categories category() {return category;}

    public String isPublic() {return isPublic;}

    public Badge badge() {return badge;}

    public Long commentCount() {return commentCount;}

    public LocalDateTime createdAt() {return createdAt;}

    public LocalDateTime updatedAt() {return updatedAt;}
}
