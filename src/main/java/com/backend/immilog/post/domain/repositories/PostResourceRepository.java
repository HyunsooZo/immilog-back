package com.backend.immilog.post.domain.repositories;

import com.backend.immilog.post.domain.enums.PostType;
import com.backend.immilog.post.domain.enums.ResourceType;
import com.backend.immilog.post.domain.model.resource.PostResource;

import java.util.List;

public interface PostResourceRepository {
    void deleteAllEntities(
            Long postSeq,
            PostType postType,
            ResourceType resourceType,
            List<String> deleteAttachments
    );

    void deleteAllByPostSeq(Long seq);

    List<PostResource> findAllByPostSeq(Long seq);
}
