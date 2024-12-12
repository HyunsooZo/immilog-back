package com.backend.immilog.post.infrastructure.result;

import com.backend.immilog.post.application.result.PostResult;
import com.backend.immilog.post.domain.enums.Categories;
import com.backend.immilog.post.domain.enums.InteractionType;
import com.backend.immilog.post.domain.enums.PostStatus;
import com.backend.immilog.post.domain.enums.ResourceType;
import com.backend.immilog.post.domain.model.post.Post;
import com.backend.immilog.post.infrastructure.jpa.entity.InteractionUserEntity;
import com.backend.immilog.post.infrastructure.jpa.entity.PostEntity;
import com.backend.immilog.post.infrastructure.jpa.entity.PostResourceEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.backend.immilog.post.domain.enums.InteractionType.BOOKMARK;
import static com.backend.immilog.post.domain.enums.InteractionType.LIKE;
import static com.backend.immilog.post.domain.enums.ResourceType.ATTACHMENT;
import static com.backend.immilog.post.domain.enums.ResourceType.TAG;

@Getter
@Setter
@ToString
public class PostEntityResult {
    private Long seq;
    private String title;
    private String content;
    private Long userSeq;
    private String userProfileUrl;
    private String userNickName;
    private List<CommentEntityResult> comments;
    private Long commentCount;
    private Long viewCount;
    private Long likeCount;
    private List<String> tags;
    private List<String> attachments;
    private List<Long> likeUsers;
    private List<Long> bookmarkUsers;
    private String isPublic;
    private String country;
    private String region;
    private Categories category;
    private PostStatus status;
    private String createdAt;

    public PostEntityResult(
            PostEntity postEntity,
            List<InteractionUserEntity> interactionUsers,
            List<PostResourceEntity> postResources
    ) {
        interactionUsers = interactionUsers != null ? interactionUsers : List.of();
        postResources = postResources != null ? postResources : List.of();
        Post post = postEntity.toDomain();

        List<Long> likeUsers = getLongs(interactionUsers, LIKE);
        List<Long> bookmarkUsers = getLongs(interactionUsers, BOOKMARK);
        List<String> tags = getStrings(postResources, TAG);
        List<String> attachments = getStrings(postResources, ATTACHMENT);

        this.seq = post.getSeq();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.userSeq = post.getUserSeq();
        this.userProfileUrl = post.getUserProfileImage();
        this.userNickName = post.getUserNickname();
        this.country = post.getCountryName();
        this.region = post.getRegion();
        this.viewCount = post.getViewCount();
        this.likeCount = post.getLikeCount();
        this.commentCount = post.getCommentCount();
        this.comments = new ArrayList<>();
        this.likeUsers = likeUsers;
        this.bookmarkUsers = bookmarkUsers;
        this.tags = tags;
        this.attachments = attachments;
        this.isPublic = post.getIsPublic();
        this.status = post.getStatus();
        this.category = post.getCategory();
        this.createdAt = post.getCreatedAt().toString();
    }

    private static List<String> getStrings(
            List<PostResourceEntity> postResources,
            ResourceType type
    ) {
        return postResources.stream()
                .filter(Objects::nonNull)
                .filter(c -> c.getResourceType().equals(type))
                .map(PostResourceEntity::getContent)
                .toList();
    }

    private static List<Long> getLongs(
            List<InteractionUserEntity> interactionUsers,
            InteractionType type
    ) {
        return interactionUsers.stream()
                .filter(Objects::nonNull)
                .filter(c -> c.getInteractionType().equals(type))
                .map(InteractionUserEntity::getUserSeq)
                .toList();
    }

    public PostResult toPostResult() {
        return PostResult.builder()
                .seq(seq)
                .title(title)
                .content(content)
                .userSeq(userSeq)
                .userProfileUrl(userProfileUrl)
                .userNickName(userNickName)
                .country(country)
                .region(region)
                .viewCount(viewCount)
                .likeCount(likeCount)
                .commentCount(commentCount)
                .comments(new ArrayList<>())
                .likeUsers(likeUsers)
                .bookmarkUsers(bookmarkUsers)
                .tags(tags)
                .attachments(attachments)
                .isPublic(isPublic)
                .status(status)
                .category(category)
                .createdAt(createdAt)
                .build();
    }

}
