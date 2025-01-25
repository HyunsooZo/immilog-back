package com.backend.immilog.post.application.services.command;

import com.backend.immilog.post.domain.model.comment.Comment;
import com.backend.immilog.post.domain.repositories.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentCommandService {
    private final CommentRepository commentRepository;

    public CommentCommandService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Transactional
    public void save(Comment comment) {
        commentRepository.save(comment);
    }
}
