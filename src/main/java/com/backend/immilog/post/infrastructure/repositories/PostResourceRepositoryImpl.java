package com.backend.immilog.post.infrastructure.repositories;

import com.backend.immilog.post.domain.enums.PostType;
import com.backend.immilog.post.domain.enums.ResourceType;
import com.backend.immilog.post.domain.model.resource.PostResource;
import com.backend.immilog.post.domain.repositories.PostResourceRepository;
import com.backend.immilog.post.infrastructure.jdbc.PostResourceJdbcRepository;
import com.backend.immilog.post.infrastructure.jpa.entity.resource.PostResourceEntity;
import com.backend.immilog.post.infrastructure.jpa.repository.PostResourceJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostResourceRepositoryImpl implements PostResourceRepository {
    private final PostResourceJdbcRepository postResourceJdbcRepository;
    private final PostResourceJpaRepository postResourceJpaRepository;

    public PostResourceRepositoryImpl(
            PostResourceJdbcRepository postResourceJdbcRepository,
            PostResourceJpaRepository postResourceJpaRepository
    ) {
        this.postResourceJdbcRepository = postResourceJdbcRepository;
        this.postResourceJpaRepository = postResourceJpaRepository;
    }

    @Override
    public void deleteAllEntities(
            Long postSeq,
            PostType postType,
            ResourceType resourceType,
            List<String> deleteAttachments
    ) {
        postResourceJdbcRepository.deleteAllEntities(
                postSeq,
                postType,
                resourceType,
                deleteAttachments
        );
    }

    @Override
    public void deleteAllByPostSeq(Long seq) {
        postResourceJdbcRepository.deleteAllByPostSeq(seq);
    }

    @Override
    public List<PostResource> findAllByPostSeq(Long seq) {
        return postResourceJpaRepository.findAllByPostSeq(seq)
                .stream()
                .map(PostResourceEntity::toDomain)
                .toList();
    }

    @Override
    public List<PostResource> findAllByPostSeqList(List<Long> postSeqList) {
        return postResourceJdbcRepository.findAllByPostSeqList(postSeqList);
    }
}
