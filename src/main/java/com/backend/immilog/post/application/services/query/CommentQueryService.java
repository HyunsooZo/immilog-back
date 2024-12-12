package com.backend.immilog.post.application.services.query;

import com.backend.immilog.post.application.result.CommentResult;
import com.backend.immilog.post.domain.repositories.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentQueryService {
    private CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public List<CommentResult> getComments(Long postSeq) {
        return commentRepository.getComments(postSeq);
    }
}
