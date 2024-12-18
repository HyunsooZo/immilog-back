package com.backend.immilog.post.application.services.query;

import com.backend.immilog.post.application.result.CommentResult;
import com.backend.immilog.post.domain.repositories.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentQueryService {
    private final CommentRepository commentRepository;

    public CommentQueryService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Transactional(readOnly = true)
    public List<CommentResult> getComments(Long postSeq) {
        return commentRepository.getComments(postSeq);
    }
}
