package com.backend.immilog.post.domain.model.post;

import com.backend.immilog.post.application.command.PostUploadCommand;
import com.backend.immilog.post.application.result.PostResult;
import com.backend.immilog.post.domain.enums.Badge;
import com.backend.immilog.post.domain.enums.Categories;
import com.backend.immilog.post.domain.enums.PostStatus;
import com.backend.immilog.post.exception.PostErrorCode;
import com.backend.immilog.post.exception.PostException;
import com.backend.immilog.user.domain.model.user.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
public class Post {
    private final Long seq;
    private final PostUserInfo postUserInfo;
    private PostInfo postInfo;
    private final Categories category;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private String isPublic;
    private Badge badge;
    private Long commentCount;

    @Builder
    public Post(
            Long seq,
            PostUserInfo postUserInfo,
            PostInfo postInfo,
            Categories category,
            String isPublic,
            Long commentCount,
            Badge badge,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.seq = seq;
        this.postUserInfo = postUserInfo;
        this.postInfo = postInfo;
        this.category = category;
        this.isPublic = isPublic;
        this.commentCount = commentCount;
        this.badge = badge;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Post of(
            PostUploadCommand postUploadCommand,
            User user
    ) {
        PostInfo postInfo = PostInfo.of(
                postUploadCommand.title(),
                postUploadCommand.content(),
                user.getCountry(),
                user.getRegion()
        );

        PostUserInfo postUserInfo = PostUserInfo.builder()
                .userSeq(user.getSeq())
                .profileImage(user.getImageUrl())
                .nickname(user.getNickname())
                .build();

        return Post.builder()
                .postUserInfo(postUserInfo)
                .postInfo(postInfo)
                .category(postUploadCommand.category())
                .isPublic(postUploadCommand.isPublic() ? "Y" : "N")
                .commentCount(0L)
                .build();
    }

    public Post updateCommentCount() {
        this.commentCount++;
        return this;
    }

    public Post updateIsPublic(Boolean isPublic) {
        if (isPublic != null) {
            String value = isPublic ? "Y" : "N";
            if (!this.isPublic.equals(value)) {
                this.isPublic = value;
            }
        }
        return this;
    }


    public Post updateContent(String content) {
        if (content == null || this.postInfo.content().equals(content)) {
            return this;
        }
        this.postInfo = new PostInfo(
                this.postInfo.title(),
                content,
                this.postInfo.viewCount(),
                this.postInfo.likeCount(),
                this.postInfo.region(),
                this.postInfo.status(),
                this.postInfo.country()
        );
        return this;
    }

    public Post updateTitle(String title) {
        if (title == null || this.postInfo.title().equals(title)) {
            return this;
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
        return this;
    }

    public Long getUserSeq() {return this.postUserInfo.userSeq();}

    public Post increaseViewCount() {
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

    public String getTitle() {return this.postInfo.title();}

    public String getContent() {return this.postInfo.content();}

    public String getUserProfileImage() {return this.postUserInfo.profileImage();}

    public String getUserNickname() {return this.postUserInfo.nickname();}

    public String getCountryName() {return this.postInfo.country().name();}

    public String getRegion() {return this.postInfo.region();}

    public Long getViewCount() {return this.postInfo.viewCount();}

    public Long getLikeCount() {return this.postInfo.likeCount();}

    public void updateBadge(Badge badge) {
        this.badge = Optional.ofNullable(badge)
                .orElseThrow(() -> new PostException(PostErrorCode.BADGE_NOT_FOUND));
    }

    public PostStatus getStatus() {return this.postInfo.status();}

    public String getProfileImage() {return this.postUserInfo.profileImage();}

    public String getNickname() {return this.postUserInfo.nickname();}

    public PostResult toResult() {
        return PostResult.builder()
                .seq(this.seq)
                .userProfileUrl(this.postUserInfo.profileImage())
                .userNickName(this.postUserInfo.nickname())
                .country(this.getCountryName())
                .region(this.getRegion())
                .category(this.category)
                .isPublic(this.isPublic)
                .commentCount(this.commentCount)
                .likeCount(this.postInfo.likeCount())
                .viewCount(this.postInfo.viewCount())
                .title(this.postInfo.title())
                .content(this.postInfo.content())
                .createdAt(this.createdAt.toString())
                .build();
    }

}
