package com.backend.immilog.post.domain.model.post;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.post.application.result.PostResult;
import com.backend.immilog.post.domain.enums.Badge;
import com.backend.immilog.post.domain.enums.Categories;
import com.backend.immilog.post.domain.enums.PostStatus;
import com.backend.immilog.post.exception.PostErrorCode;
import com.backend.immilog.post.exception.PostException;
import com.backend.immilog.user.domain.model.user.User;

import java.time.LocalDateTime;

public record Post(
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
    public static Post of(
            User user,
            String title,
            String content,
            Categories category,
            String isPublic
    ) {
        PostInfo postInfo = PostInfo.of(
                title,
                content,
                user.country(),
                user.region()
        );

        PostUserInfo postUserInfo = new PostUserInfo(
                user.seq(),
                user.imageUrl(),
                user.nickname()
        );

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

    public Post updateCommentCount() {
        return new Post(
                this.seq,
                this.postUserInfo,
                this.postInfo,
                this.category,
                this.isPublic,
                this.badge,
                this.commentCount + 1,
                this.createdAt,
                this.updatedAt
        );
    }

    public Post updateIsPublic(Boolean isPublic) {
        String value = isPublic ? "Y" : "N";
        if (this.isPublic.equals(value)) {
            return this;
        }
        return new Post(
                this.seq,
                this.postUserInfo,
                this.postInfo,
                this.category,
                isPublic ? "Y" : "N",
                this.badge,
                this.commentCount,
                this.createdAt,
                this.updatedAt
        );
    }

    public Post updateContent(String newContent) {
        if (newContent == null || this.postInfo.content().equals(newContent)) {
            return this;
        }
        return new Post(
                this.seq,
                this.postUserInfo,
                new PostInfo(
                        this.postInfo.title(),
                        newContent,
                        this.postInfo.viewCount(),
                        this.postInfo.likeCount(),
                        this.postInfo.region(),
                        this.postInfo.status(),
                        this.postInfo.country()
                ),
                this.category,
                this.isPublic,
                this.badge,
                this.commentCount,
                this.createdAt,
                this.updatedAt
        );
    }

    public Post updateTitle(String title) {
        if (title == null || this.postInfo.title().equals(title)) {
            return this;
        }
        return new Post(
                this.seq,
                this.postUserInfo,
                new PostInfo(
                        title,
                        this.postInfo.content(),
                        this.postInfo.viewCount(),
                        this.postInfo.likeCount(),
                        this.postInfo.region(),
                        this.postInfo.status(),
                        this.postInfo.country()
                ),
                this.category,
                this.isPublic,
                this.badge,
                this.commentCount,
                this.createdAt,
                this.updatedAt
        );
    }

    public Post delete() {
        if (this.postInfo.status() == PostStatus.DELETED) {
            throw new PostException(PostErrorCode.POST_ALREADY_DELETED);
        }
        return new Post(
                this.seq,
                this.postUserInfo,
                new PostInfo(
                        this.postInfo.title(),
                        this.postInfo.content(),
                        this.postInfo.viewCount(),
                        this.postInfo.likeCount(),
                        this.postInfo.region(),
                        PostStatus.DELETED,
                        this.postInfo.country()
                ),
                this.category,
                this.isPublic,
                this.badge,
                this.commentCount,
                this.createdAt,
                LocalDateTime.now()
        );
    }

    public Long userSeq() {return this.postUserInfo.userSeq();}

    public Post increaseViewCount() {
        return new Post(
                this.seq,
                this.postUserInfo,
                new PostInfo(
                        this.postInfo.title(),
                        this.postInfo.content(),
                        this.postInfo.viewCount() + 1,
                        this.postInfo.likeCount(),
                        this.postInfo.region(),
                        this.postInfo.status(),
                        this.postInfo.country()
                ),
                this.category,
                this.isPublic,
                this.badge,
                this.commentCount,
                this.createdAt,
                this.updatedAt
        );
    }

    public Post updateBadge(Badge badge) {
        return new Post(
                this.seq,
                this.postUserInfo,
                this.postInfo,
                this.category,
                this.isPublic,
                badge,
                this.commentCount,
                this.createdAt,
                this.updatedAt
        );
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
        return new Post(
                postResult.getSeq(),
                new PostUserInfo(
                        postResult.getUserSeq(),
                        postResult.getUserProfileUrl(),
                        postResult.getUserNickName()
                ),
                new PostInfo(
                        postResult.getTitle(),
                        postResult.getContent(),
                        postResult.getViewCount(),
                        postResult.getLikeCount(),
                        postResult.getRegion(),
                        postResult.getStatus(),
                        Country.valueOf(postResult.getCountry())
                ),
                postResult.getCategory(),
                postResult.getIsPublic(),
                null,
                postResult.getCommentCount(),
                LocalDateTime.parse(postResult.getCreatedAt()),
                LocalDateTime.parse(postResult.getUpdatedAt())
        );
    }
}
