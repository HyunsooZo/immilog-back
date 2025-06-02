package com.backend.immilog.post.application.result;

import com.backend.immilog.post.domain.model.post.Categories;
import com.backend.immilog.post.domain.model.post.PostStatus;
import com.backend.immilog.post.presentation.response.PostSingleResponse;
import org.springframework.http.HttpStatus;

import java.util.List;

public record PostResult(
        Long seq,
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
        String title,
        String content,
        String keyword
) {
    public PostSingleResponse toResponse() {
        return new PostSingleResponse(HttpStatus.OK.value(), "success", this);
    }
}
