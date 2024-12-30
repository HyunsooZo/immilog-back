package com.backend.immilog.post.domain.model.post;

import com.backend.immilog.post.application.command.PostUploadCommand;
import com.backend.immilog.post.domain.enums.Badge;
import com.backend.immilog.post.domain.enums.Categories;
import com.backend.immilog.post.domain.enums.PostStatus;
import com.backend.immilog.post.exception.PostErrorCode;
import com.backend.immilog.post.exception.PostException;
import com.backend.immilog.user.domain.model.user.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Post {
    private final Long seq;
    private final PostUserInfo postUserInfo;
    private final PostInfo postInfo;
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

    public void updateCommentCount() {
        this.commentCount++;
    }

    public void updateIsPublic(String isPublic) {
        this.isPublic = isPublic;
    }

    public void updateContent(String content) {
        this.postInfo.updateContent(content);
    }

    public void updateTitle(String title) {
        this.postInfo.updateTitle(title);
    }

    public void delete() {
        this.getPostInfo().delete();
    }

    public Long getUserSeq() {
        return this.postUserInfo.getUserSeq();
    }

    public void increaseViewCount() {
        this.postInfo.increaseViewCount();
    }

    public String getTitle() {
        return this.postInfo.getTitle();
    }

    public String getContent() {
        return this.postInfo.getContent();
    }

    public String getUserProfileImage() {
        return this.postUserInfo.getProfileImage();
    }

    public String getUserNickname() {
        return this.postUserInfo.getNickname();
    }

    public String getCountryName() {return this.postInfo.getCountry().name();}

    public String getRegion() {
        return this.postInfo.getRegion();
    }

    public Long getViewCount() {
        return this.postInfo.getViewCount();
    }

    public Long getLikeCount() {
        return this.postInfo.getLikeCount();
    }

    public void updateBadge(Badge badge) {
        if (badge == null) {
            throw new PostException(PostErrorCode.BADGE_NOT_FOUND);
        }
        this.badge = badge;
    }

    public PostStatus getStatus() {
        return this.postInfo.getStatus();
    }
}
