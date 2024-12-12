package com.backend.immilog.post.application.services.command;

import com.backend.immilog.post.domain.model.comment.Comment;
import com.backend.immilog.post.domain.repositories.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentCommandService {
    private final CommentRepository commentRepository;

    @Transactional
    public void save(Comment comment) {
        commentRepository.save(comment);
    }
}
