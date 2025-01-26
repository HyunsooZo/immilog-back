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
    private final PostData postData;
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
            PostData postData,
            Categories category,
            String isPublic,
            Long commentCount,
            Badge badge,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.seq = seq;
        this.postUserInfo = postUserInfo;
        this.postData = postData;
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
        PostData postData = PostData.of(
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
                .postData(postData)
                .category(postUploadCommand.category())
                .isPublic(postUploadCommand.isPublic() ? "Y" : "N")
                .commentCount(0L)
                .build();
    }

    public void updateCommentCount() {
        this.commentCount++;
    }

    public void updateIsPublic(String isPublic) {this.isPublic = isPublic;}

    public void updateContent(String content) {this.postData.updateContent(content);}

    public void updateTitle(String title) {this.postData.updateTitle(title);}

    public void delete() {this.getPostData().delete();}

    public Long getUserSeq() {return this.postUserInfo.getUserSeq();}

    public void increaseViewCount() {this.postData.increaseViewCount();}

    public String getTitle() {return this.postData.getTitle();}

    public String getContent() {return this.postData.getContent();}

    public String getUserProfileImage() {return this.postUserInfo.getProfileImage();}

    public String getUserNickname() {return this.postUserInfo.getNickname();}

    public String getCountryName() {return this.postData.getCountry().name();}

    public String getRegion() {return this.postData.getRegion();}

    public Long getViewCount() {return this.postData.getViewCount();}

    public Long getLikeCount() {return this.postData.getLikeCount();}

    public void updateBadge(Badge badge) {
        this.badge = Optional.ofNullable(badge)
                .orElseThrow(() -> new PostException(PostErrorCode.BADGE_NOT_FOUND));
    }

    public PostStatus getStatus() {return this.postData.getStatus();}

    public String getProfileImage() {return this.postUserInfo.getProfileImage();}

    public String getNickname() {return this.postUserInfo.getNickname();}

    public PostResult toResult() {
        return PostResult.builder()
                .seq(this.seq)
                .userProfileUrl(this.postUserInfo.getProfileImage())
                .userNickName(this.postUserInfo.getNickname())
                .country(this.getCountryName())
                .region(this.getRegion())
                .category(this.category)
                .isPublic(this.isPublic)
                .commentCount(this.commentCount)
                .likeCount(this.postData.getLikeCount())
                .viewCount(this.postData.getViewCount())
                .title(this.postData.getTitle())
                .content(this.postData.getContent())
                .createdAt(this.createdAt.toString())
                .build();
    }

}
