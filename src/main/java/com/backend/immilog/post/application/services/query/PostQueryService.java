package com.backend.immilog.post.application.services.query;

import com.backend.immilog.post.application.result.PostResult;
import com.backend.immilog.post.domain.enums.Categories;
import com.backend.immilog.post.domain.enums.Countries;
import com.backend.immilog.post.domain.enums.SortingMethods;
import com.backend.immilog.post.domain.model.post.Post;
import com.backend.immilog.post.domain.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostQueryService {
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public Optional<Post> getPostById(Long postSeq) {
        return postRepository.getById(postSeq);
    }

    @Transactional(readOnly = true)
    public Page<PostResult> getPosts(
            Countries country,
            SortingMethods sortingMethod,
            String isPublic,
            Categories category,
            Pageable pageable
    ) {
        return postRepository.getPosts(
                country,
                sortingMethod,
                isPublic,
                category,
                pageable
        );
    }

    @Transactional(readOnly = true)
    public Page<PostResult> getPostsByKeyword(
            String keyword,
            PageRequest pageRequest
    ) {
        return postRepository.getPostsByKeyword(keyword, pageRequest);
    }

    @Transactional(readOnly = true)
    public Optional<PostResult> getPost(Long postSeq) {
        return postRepository.getPost(postSeq);
    }

    @Transactional(readOnly = true)
    public Page<PostResult> getPostsByUserSeq(
            Long userSeq,
            Pageable pageable
    ) {
        return postRepository.getPostsByUserSeq(userSeq, pageable);
    }
}
