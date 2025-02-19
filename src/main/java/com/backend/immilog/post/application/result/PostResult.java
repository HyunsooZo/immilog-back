package com.backend.immilog.post.application.result;

import com.backend.immilog.post.domain.enums.Categories;
import com.backend.immilog.post.domain.enums.InteractionType;
import com.backend.immilog.post.domain.enums.PostStatus;
import com.backend.immilog.post.domain.enums.ResourceType;
import com.backend.immilog.post.domain.model.interaction.InteractionUser;
import com.backend.immilog.post.domain.model.resource.PostResource;
import com.backend.immilog.post.presentation.response.PostSingleResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class PostResult {
    private final Long seq;
    private final Long userSeq;
    private final String userProfileUrl;
    private final String userNickName;
    private final List<CommentResult> comments;
    private final Long commentCount;
    private final Long viewCount;
    private Long likeCount;
    private final List<String> tags;
    private final List<String> attachments;
    private final List<Long> likeUsers;
    private final List<Long> bookmarkUsers;
    private final String isPublic;
    private final String country;
    private final String region;
    private final Categories category;
    private final PostStatus status;
    private final String createdAt;
    private final String updatedAt;
    private String title;
    private String content;
    private String keyword;

    public PostResult(
            Long seq,
            String title,
            String content,
            Long userSeq,
            String userProfileUrl,
            String userNickName,
            List<CommentResult> comments,
            Long commentCount,
            Long viewCount,
            Long likeCount,
            List<String> tags,
            List<String> attachments,
            List<Long> likeUsers,
            List<Long> bookmarkUsers,
            String isPublic,
            String country,
            String region,
            Categories category,
            PostStatus status,
            String createdAt,
            String updatedAt,
            String keyword
    ) {
        this.seq = seq;
        this.title = title;
        this.content = content;
        this.userSeq = userSeq;
        this.userProfileUrl = userProfileUrl;
        this.userNickName = userNickName;
        this.comments = Optional.ofNullable(comments).orElseGet(ArrayList::new);
        this.commentCount = commentCount;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.tags = Optional.ofNullable(tags).orElseGet(ArrayList::new);
        this.attachments = Optional.ofNullable(attachments).orElseGet(ArrayList::new);
        this.likeUsers = Optional.ofNullable(likeUsers).orElseGet(ArrayList::new);
        this.bookmarkUsers = Optional.ofNullable(bookmarkUsers).orElseGet(ArrayList::new);
        this.isPublic = isPublic;
        this.country = country;
        this.region = region;
        this.category = category;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.keyword = keyword;
    }

    public PostResult(
            Long seq,
            Long userSeq,
            String userProfileUrl,
            String userNickName,
            Long commentCount,
            Long viewCount,
            Long likeCount,
            String isPublic,
            String country,
            String region,
            Categories category,
            PostStatus status,
            String title,
            String content,
            String createdAt,
            String updatedAt
    ) {
        this.seq = seq;
        this.userSeq = userSeq;
        this.userProfileUrl = userProfileUrl;
        this.userNickName = userNickName;
        this.commentCount = commentCount;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.isPublic = isPublic;
        this.country = country;
        this.region = region;
        this.category = category;
        this.status = status;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.comments = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.attachments = new ArrayList<>();
        this.likeUsers = new ArrayList<>();
        this.bookmarkUsers = new ArrayList<>();
    }

    private static String extractKeyword(
            String text,
            String keyword,
            int after,
            int before
    ) {
        if (text == null || keyword == null) {
            return "";
        }
        int keywordIndex = text.indexOf(keyword);
        if (keywordIndex == -1) {
            return text.substring(0, Math.min(text.length(), after));
        }
        int start = Math.max(keywordIndex - before, 0);
        int end = Math.min(keywordIndex + keyword.length() + after, text.length());
        return text.substring(start, end);
    }

    private static List<String> extractTags(
            List<String> tags,
            String keyword
    ) {
        if (tags == null || tags.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> keywordTags = tags.stream()
                .filter(tag -> tag.contains(keyword))
                .limit(1)
                .toList();

        List<String> shuffledTags = tags.stream()
                .filter(tag -> !keywordTags.contains(tag))
                .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
                    Collections.shuffle(list);
                    return list.stream().limit(keywordTags.isEmpty() ? 3 : 2).toList();
                }));

        return new ArrayList<>(keywordTags) {{
            addAll(shuffledTags);
        }};
    }

    public void addComments(List<CommentResult> comments) {
        if (comments != null) {
            this.comments.addAll(comments);
        }
    }

    public void addKeywords(String keyword) {
        if (keyword == null) {
            return;
        }
        this.title = extractKeyword(this.title, keyword, 20, 5);
        this.content = extractKeyword(this.content, keyword, 50, 5);
        this.tags.addAll(extractTags(this.tags, keyword));
        this.keyword = keyword;
    }

    public void addInteractionUsers(List<InteractionUser> interactionUsers) {
        if (interactionUsers == null || interactionUsers.isEmpty()) {
            return;
        }

        this.likeUsers.addAll(interactionUsers.stream()
                .filter(u -> u.interactionType() == InteractionType.LIKE)
                .map(InteractionUser::userSeq)
                .toList());

        this.bookmarkUsers.addAll(interactionUsers.stream()
                .filter(u -> u.interactionType() == InteractionType.BOOKMARK)
                .map(InteractionUser::userSeq)
                .toList());
    }

    public void addResources(List<PostResource> resources) {
        if (resources == null || resources.isEmpty()) {
            return;
        }

        this.tags.addAll(resources.stream()
                .filter(r -> r.resourceType() == ResourceType.TAG)
                .map(PostResource::content)
                .toList());

        this.attachments.addAll(resources.stream()
                .filter(r -> r.resourceType() == ResourceType.ATTACHMENT)
                .map(PostResource::content)
                .toList());
    }

    public PostSingleResponse toResponse() {
        return new PostSingleResponse(HttpStatus.OK.value(), "success", this);
    }

    public void updateLikeCount(int size) {
        this.likeCount = (long) size;
    }
}
