package com.backend.immilog.post.domain.repositories;

import com.backend.immilog.post.application.result.CommentResult;
import com.backend.immilog.post.domain.model.comment.Comment;

import java.util.List;

public interface CommentRepository {
    List<CommentResult> getComments(Long postSeq);

    void save(Comment comment);

    Comment findById(Long commentSeq);
}
