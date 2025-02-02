package com.backend.immilog.post.infrastructure.repositories;

import com.backend.immilog.post.application.result.CommentResult;
import com.backend.immilog.post.domain.model.comment.Comment;
import com.backend.immilog.post.domain.repositories.CommentRepository;
import com.backend.immilog.post.infrastructure.jdbc.CommentJdbcRepository;
import com.backend.immilog.post.infrastructure.jpa.entity.comment.CommentEntity;
import com.backend.immilog.post.infrastructure.jpa.repository.CommentJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentRepositoryImpl implements CommentRepository {
    private final CommentJdbcRepository commentJdbcRepository;
    private final CommentJpaRepository commentJpaRepository;

    public CommentRepositoryImpl(
            CommentJdbcRepository commentJdbcRepository,
            CommentJpaRepository commentJpaRepository
    ) {
        this.commentJdbcRepository = commentJdbcRepository;
        this.commentJpaRepository = commentJpaRepository;
    }

    @Override
    public List<CommentResult> getComments(Long postSeq) {
        return commentJdbcRepository
                .getComments(postSeq)
                .stream()
                .toList();
    }

    @Override
    public void save(Comment comment) {
        commentJpaRepository.save(CommentEntity.from(comment));
    }
}


