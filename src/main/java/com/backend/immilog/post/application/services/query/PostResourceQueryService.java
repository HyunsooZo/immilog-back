package com.backend.immilog.post.application.services.query;

import com.backend.immilog.post.domain.enums.PostType;
import com.backend.immilog.post.domain.model.resource.PostResource;
import com.backend.immilog.post.domain.repositories.PostResourceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostResourceQueryService {
    private final PostResourceRepository postResourceRepository;

    public PostResourceQueryService(PostResourceRepository postResourceRepository) {
        this.postResourceRepository = postResourceRepository;
    }

    @Transactional(readOnly = true)
    public List<PostResource> getResourcesByPostSeqList(
            List<Long> postSeqList,
            PostType postType
    ) {
        return postResourceRepository.findAllByPostSeqList(postSeqList, postType);
    }
}
