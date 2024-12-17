package com.backend.immilog.post.application.services.command;

import com.backend.immilog.global.aop.PerformanceMonitor;
import com.backend.immilog.post.domain.enums.PostType;
import com.backend.immilog.post.domain.enums.ResourceType;
import com.backend.immilog.post.domain.repositories.PostResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostResourceCommandService {
    private final PostResourceRepository postResourceRepository;

    @PerformanceMonitor
    @Transactional
    public void deleteAllEntities(
            Long postSeq,
            PostType postType,
            ResourceType resourceType,
            List<String> deleteResources
    ) {
        postResourceRepository.deleteAllEntities(
                postSeq,
                postType,
                resourceType,
                deleteResources
        );
    }

    @Transactional
    public void deleteAllByPostSeq(Long seq) {
        postResourceRepository.deleteAllByPostSeq(seq);
    }
}
