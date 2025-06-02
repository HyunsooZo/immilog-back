package com.backend.immilog.post.application.mapper;

import com.backend.immilog.post.application.result.CommentResult;
import com.backend.immilog.post.application.result.PostResult;
import com.backend.immilog.post.domain.model.interaction.InteractionType;
import com.backend.immilog.post.domain.model.interaction.InteractionUser;
import com.backend.immilog.post.domain.model.resource.PostResource;
import com.backend.immilog.post.domain.model.resource.ResourceType;
import com.backend.immilog.post.presentation.response.PostSingleResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostResultAssembler {

    public PostResult assembleComments(
            PostResult postResult,
            List<CommentResult> comments
    ) {
        if (comments != null) {
            postResult.comments().addAll(comments);
        }
        return postResult;
    }

    public PostResult assembleKeywords(
            PostResult postResult,
            String keyword
    ) {
        if (keyword == null) {
            return postResult;
        }
        var updatedTags = new ArrayList<>(postResult.tags());
        updatedTags.addAll(extractTags(updatedTags, keyword));
        return new PostResult(
                postResult.seq(),
                postResult.userSeq(),
                postResult.userProfileUrl(),
                postResult.userNickName(),
                postResult.comments(),
                postResult.commentCount(),
                postResult.viewCount(),
                postResult.likeCount(),
                updatedTags,
                new ArrayList<>(postResult.attachments()),
                new ArrayList<>(postResult.likeUsers()),
                new ArrayList<>(postResult.bookmarkUsers()),
                postResult.isPublic(),
                postResult.country(),
                postResult.region(),
                postResult.category(),
                postResult.status(),
                postResult.createdAt(),
                postResult.updatedAt(),
                extractKeyword(postResult.title(), keyword, 20, 5),
                extractKeyword(postResult.content(), keyword, 50, 5),
                keyword
        );
    }

    public PostResult assembleInteractionUsers(
            PostResult postResult,
            List<InteractionUser> interactionUsers
    ) {
        if (interactionUsers == null || interactionUsers.isEmpty()) {
            return postResult;
        }
        var newLikeUsers = new ArrayList<>(postResult.likeUsers());
        var newBookmarkUsers = new ArrayList<>(postResult.bookmarkUsers());
        newLikeUsers.addAll(interactionUsers.stream()
                .filter(u -> u.interactionType() == InteractionType.LIKE)
                .map(InteractionUser::userSeq)
                .toList());
        newBookmarkUsers.addAll(interactionUsers.stream()
                .filter(u -> u.interactionType() == InteractionType.BOOKMARK)
                .map(InteractionUser::userSeq)
                .toList());
        return new PostResult(
                postResult.seq(),
                postResult.userSeq(),
                postResult.userProfileUrl(),
                postResult.userNickName(),
                postResult.comments(),
                postResult.commentCount(),
                postResult.viewCount(),
                postResult.likeCount(),
                postResult.tags(),
                postResult.attachments(),
                newLikeUsers,
                newBookmarkUsers,
                postResult.isPublic(),
                postResult.country(),
                postResult.region(),
                postResult.category(),
                postResult.status(),
                postResult.createdAt(),
                postResult.updatedAt(),
                postResult.title(),
                postResult.content(),
                postResult.keyword()
        );
    }

    public PostResult assembleResources(
            PostResult postResult,List<PostResource> resources) {
        if (resources == null || resources.isEmpty()) {
            return postResult;
        }

        var updatedTags = new ArrayList<>(postResult.tags());
        updatedTags.addAll(resources.stream()
                .filter(r -> r.resourceType() == ResourceType.TAG)
                .map(PostResource::content)
                .toList());

        var updatedAttachments = new ArrayList<>(postResult.attachments());
        updatedAttachments.addAll(resources.stream()
                .filter(r -> r.resourceType() == ResourceType.ATTACHMENT)
                .map(PostResource::content)
                .toList());
        return new PostResult(
                postResult.seq(),
                postResult.userSeq(),
                postResult.userProfileUrl(),
                postResult.userNickName(),
                postResult.comments(),
                postResult.commentCount(),
                postResult.viewCount(),
                postResult.likeCount(),
                updatedTags,
                updatedAttachments,
                postResult.likeUsers(),
                postResult.bookmarkUsers(),
                postResult.isPublic(),
                postResult.country(),
                postResult.region(),
                postResult.category(),
                postResult.status(),
                postResult.createdAt(),
                postResult.updatedAt(),
                postResult.title(),
                postResult.content(),
                postResult.keyword()
        );
    }

    public PostResult assembleLikeCount(PostResult postResult, int size) {
        return new PostResult(
                postResult.seq(),
                postResult.userSeq(),
                postResult.userProfileUrl(),
                postResult.userNickName(),
                postResult.comments(),
                postResult.commentCount(),
                postResult.viewCount(),
                (long) size,
                postResult.tags(),
                postResult.attachments(),
                postResult.likeUsers(),
                postResult.bookmarkUsers(),
                postResult.isPublic(),
                postResult.country(),
                postResult.region(),
                postResult.category(),
                postResult.status(),
                postResult.createdAt(),
                postResult.updatedAt(),
                postResult.title(),
                postResult.content(),
                postResult.keyword()
        );
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

        var keywordTags = tags.stream()
                .filter(tag -> tag.contains(keyword))
                .limit(1)
                .toList();

        var shuffledTags = tags.stream()
                .filter(tag -> !keywordTags.contains(tag))
                .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
                    Collections.shuffle(list);
                    return list.stream().limit(keywordTags.isEmpty() ? 3 : 2).toList();
                }));

        return new ArrayList<>(keywordTags) {{
            addAll(shuffledTags);
        }};
    }
}
