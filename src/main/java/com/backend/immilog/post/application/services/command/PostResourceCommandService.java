package com.backend.immilog.post.application.services.command;

import com.backend.immilog.global.aop.monitor.PerformanceMonitor;
import com.backend.immilog.post.domain.enums.PostType;
import com.backend.immilog.post.domain.enums.ResourceType;
import com.backend.immilog.post.domain.repositories.PostResourceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostResourceCommandService {
    private final PostResourceRepository postResourceRepository;

    public PostResourceCommandService(PostResourceRepository postResourceRepository) {
        this.postResourceRepository = postResourceRepository;
    }

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
