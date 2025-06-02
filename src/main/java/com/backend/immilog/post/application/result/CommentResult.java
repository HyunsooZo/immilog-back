package com.backend.immilog.post.application.result;

import com.backend.immilog.post.domain.model.comment.Comment;
import com.backend.immilog.post.domain.model.post.PostStatus;
import com.backend.immilog.user.application.result.UserInfoResult;
import com.backend.immilog.user.domain.model.user.User;

import java.util.ArrayList;
import java.util.List;

public record CommentResult(
        Long seq,
        UserInfoResult user,
        String content,
        List<CommentResult> replies,
        int upVotes,
        int downVotes,
        int replyCount,
        List<Long> likeUsers,
        PostStatus status,
        String createdAt
) {
    public CommentResult copyWithNewLikeCount(Integer likeCount) {
        return new CommentResult(
                seq,
                user,
                content,
                replies,
                likeCount,
                downVotes,
                replyCount,
                likeUsers,
                status,
                createdAt
        );
    }

    public CommentResult copyWithNewLikeUsers(List<Long> likeUsers) {
        return new CommentResult(
                seq,
                user,
                content,
                replies,
                upVotes,
                downVotes,
                replyCount,
                likeUsers,
                status,
                createdAt
        );
    }
}
