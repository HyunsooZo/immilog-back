package com.backend.immilog.post.application.result;

import com.backend.immilog.post.domain.enums.PostStatus;
import com.backend.immilog.post.domain.model.comment.Comment;
import com.backend.immilog.user.application.result.UserInfoResult;
import com.backend.immilog.user.domain.model.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public static CommentResult of(
            Comment comment,
            User user
    ) {
        return new CommentResult(
                comment.seq(),
                UserInfoResult.from(user),
                comment.content(),
                new ArrayList<>(),
                comment.likeCount(),
                0,
                comment.replyCount(),
                comment.likeUsers(),
                comment.status(),
                comment.createdAt().toString()
        );
    }

    private List<CommentResult> combineReplies(
            List<Comment> replies,
            List<User> replyUsers
    ) {
        if (replies.isEmpty() || replyUsers.isEmpty()) {
            return List.of();
        }
        return replies
                .stream()
                .map(reply -> replyUsers.stream()
                        .filter(Objects::nonNull)
                        .filter(u -> u.hasSameSeq(reply.userSeq()))
                        .findFirst()
                        .map(replyUser -> CommentResult.of(reply, replyUser))
                        .orElse(null))
                .filter(Objects::nonNull)
                .toList();
    }

    public void addChildComment(CommentResult childComment) {
        this.replies.add(childComment);
    }

    public CommentResult updateLikeCount(Integer likeCount) {
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

    public CommentResult updateLikeUsers(List<Long> likeUsers) {
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
