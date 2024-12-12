package com.backend.immilog.post.infrastructure.result;

import com.backend.immilog.post.application.result.CommentResult;
import com.backend.immilog.post.domain.enums.PostStatus;
import com.backend.immilog.post.domain.model.comment.Comment;
import com.backend.immilog.user.application.result.UserInfoResult;
import com.backend.immilog.user.domain.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentEntityResult {
    private Long seq;
    private UserInfoResult user;
    private String content;
    private List<CommentEntityResult> replies;
    private int upVotes;
    private int downVotes;
    private int replyCount;
    private List<Long> likeUsers;
    private PostStatus status;
    private LocalDateTime createdAt;

    public void addChildComment(CommentEntityResult childComment) {
        this.replies.add(childComment);
        this.replyCount = this.replies.size(); // 자식 댓글 수 업데이트
    }

    public static CommentEntityResult of(
            Comment comment,
            User user
    ) {
        return CommentEntityResult.builder()
                .seq(comment.getSeq())
                .user(UserInfoResult.from(user))
                .content(comment.getContent())
                .replies(new ArrayList<>())
                .upVotes(comment.getLikeCount())
                .replyCount(comment.getReplyCount())
                .likeUsers(comment.getLikeUsers())
                .status(comment.getStatus())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    public CommentResult toCommentResult() {
        return CommentResult.builder()
                .seq(seq)
                .user(user)
                .content(content)
                .replies(replies.stream().map(CommentEntityResult::toCommentResult).toList())
                .upVotes(upVotes)
                .downVotes(downVotes)
                .replyCount(replyCount)
                .likeUsers(likeUsers)
                .status(status)
                .createdAt(createdAt)
                .build();
    }
}
