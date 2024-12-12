package com.backend.immilog.post.application.result;

import com.backend.immilog.post.domain.enums.Categories;
import com.backend.immilog.post.domain.enums.PostStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PostResult(
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
    public PostResult copyWithNewComments(
            List<CommentResult> comments
    ) {
        return PostResult.builder()
                .seq(this.seq())
                .title(this.title())
                .content(this.content())
                .userSeq(this.userSeq())
                .userProfileUrl(this.userProfileUrl())
                .userNickName(this.userNickName())
                .comments(comments)
                .commentCount(this.commentCount())
                .viewCount(this.viewCount())
                .likeCount(this.likeCount())
                .tags(this.tags())
                .attachments(this.attachments())
                .likeUsers(this.likeUsers())
                .bookmarkUsers(this.bookmarkUsers())
                .isPublic(this.isPublic())
                .country(this.country())
                .region(this.region())
                .category(this.category())
                .status(this.status())
                .createdAt(this.createdAt())
                .keyword(null)
                .build();
    }

    public PostResult copyWithKeyword(
            String keyword
    ) {
        String contentResult = extractKeyword(this.content, keyword, 50, 5);
        String titleResult = extractKeyword(this.title(), keyword, 20, 5);
        List<String> tagResult = extractTags(this.tags(), keyword);
        return PostResult.builder()
                .seq(this.seq())
                .title(titleResult)
                .content(contentResult)
                .userSeq(this.userSeq())
                .userProfileUrl(this.userProfileUrl())
                .userNickName(this.userNickName())
                .comments(this.comments())
                .commentCount(this.commentCount())
                .viewCount(this.viewCount())
                .likeCount(this.likeCount())
                .tags(tagResult)
                .attachments(this.attachments())
                .likeUsers(this.likeUsers())
                .bookmarkUsers(this.bookmarkUsers())
                .isPublic(this.isPublic())
                .country(this.country())
                .region(this.region())
                .category(this.category())
                .status(this.status())
                .createdAt(this.createdAt())
                .keyword(keyword)
                .build();
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
                        Collectors.toList(), collected -> {
                            Collections.shuffle(collected);
                            return collected.stream()
                                    .limit(isAdded ?
                                            Math.min(2, collected.size()) :
                                            Math.min(3, collected.size())
                                    ).toList();
                        }
                ));

        List<String> result = new ArrayList<>(keywordTagList);
        result.addAll(shuffledTags);
        return result;
    }

}
