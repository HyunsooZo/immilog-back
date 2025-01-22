package com.backend.immilog.post.application.result;

import com.backend.immilog.post.domain.enums.Categories;
import com.backend.immilog.post.domain.enums.PostStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class PostResult {
    private final Long seq;
    private final Long userSeq;
    private final String userProfileUrl;
    private final String userNickName;
    private final List<CommentResult> comments = new ArrayList<>();
    private final Long commentCount;
    private final Long viewCount;
    private final Long likeCount;
    private final List<String> tags;
    private final List<String> attachments = new ArrayList<>();
    private final List<Long> likeUsers;
    private final List<Long> bookmarkUsers;
    private final String isPublic;
    private final String country;
    private final String region;
    private final Categories category;
    private final PostStatus status;
    private final String createdAt;
    private String title;
    private String content;
    private String keyword;

    @Builder
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
            String keyword
    ) {
        this.seq = seq;
        this.title = title;
        this.content = content;
        this.userSeq = userSeq;
        this.userProfileUrl = userProfileUrl;
        this.userNickName = userNickName;
        if (comments != null && !comments.isEmpty()) {
            this.comments.addAll(comments);
        }
        this.commentCount = commentCount;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.tags = tags;
        if (attachments != null && !attachments.isEmpty()) {
            this.attachments.addAll(attachments);
        }
        this.likeUsers = likeUsers;
        this.bookmarkUsers = bookmarkUsers;
        this.isPublic = isPublic;
        this.country = country;
        this.region = region;
        this.category = category;
        this.status = status;
        this.createdAt = createdAt;
        this.keyword = keyword;
    }

    private static String extractKeyword(
            String text,
            String keyword,
            int after,
            int before
    ) {
        int keywordIndex = text.indexOf(keyword);
        if (keywordIndex != -1) {
            int start = Math.max(keywordIndex - before, 0);
            int end = Math.min(keywordIndex + keyword.length() + after, text.length());
            return text.substring(start, end);
        } else {
            return text.substring(0, Math.min(text.length(), after));
        }
    }

    private static List<String> extractTags(
            List<String> tags,
            String keyword
    ) {
        if (tags.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> keywordTagList = tags.stream()
                .filter(tag -> tag.contains(keyword))
                .limit(1)
                .toList();
        boolean isAdded = !keywordTagList.isEmpty();
        List<String> shuffledTags = tags.stream()
                .filter(tag -> !keywordTagList.contains(tag)) // 이미 추가된 태그는 제외
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        collected -> {
                            Collections.shuffle(collected);
                            return collected.stream().limit(isAdded ? Math.min(2, collected.size()) : Math.min(3, collected.size())).toList();
                        }
                ));

        List<String> result = new ArrayList<>(keywordTagList);
        result.addAll(shuffledTags);
        return result;
    }

    public void addComments(List<CommentResult> comments) {
        this.comments.addAll(comments);
    }

    public void addKeywords(String keyword) {
        String contentResult = extractKeyword(this.content, keyword, 50, 5);
        String titleResult = extractKeyword(this.title, keyword, 20, 5);
        List<String> tagResult = extractTags(this.tags, keyword);
        this.title = titleResult;
        this.content = contentResult;
        this.tags.addAll(tagResult);
        this.keyword = keyword;
    }
}
