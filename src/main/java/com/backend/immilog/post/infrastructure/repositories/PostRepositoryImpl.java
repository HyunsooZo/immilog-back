package com.backend.immilog.post.infrastructure.repositories;

import com.backend.immilog.post.application.result.PostResult;
import com.backend.immilog.post.domain.enums.Categories;
import com.backend.immilog.post.domain.enums.Countries;
import com.backend.immilog.post.domain.enums.SortingMethods;
import com.backend.immilog.post.domain.model.Post;
import com.backend.immilog.post.domain.repositories.PostRepository;
import com.backend.immilog.post.infrastructure.jpa.entity.PostEntity;
import com.backend.immilog.post.infrastructure.jpa.repository.PostJpaRepository;
import com.backend.immilog.post.infrastructure.querydsl.PostQueryDslRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PostRepositoryImpl implements PostRepository {
    private final PostQueryDslRepository postQueryDslRepository;
    private final PostJpaRepository postJpaRepository;

    public PostRepositoryImpl(
            PostQueryDslRepository postQueryDslRepository,
            PostJpaRepository postJpaRepository
    ) {
        this.postQueryDslRepository = postQueryDslRepository;
        this.postJpaRepository = postJpaRepository;
    }

    @Override
    public Page<PostResult> getPosts(
            Countries country,
            SortingMethods sortingMethod,
            String isPublic,
            Categories category,
            Pageable pageable
    ) {
        return postQueryDslRepository.getPosts(country, sortingMethod, isPublic, category, pageable);
    }

    @Override
    public Optional<PostResult> getPost(Long postSeq) {
        return postQueryDslRepository.getPost(postSeq);
    }

    @Override
    public Page<PostResult> getPostsByKeyword(
            String keyword,
            Pageable pageable
    ) {
        return postQueryDslRepository.getPostsByKeyword(keyword, pageable);
    }

    @Override
    public Page<PostResult> getPostsByUserSeq(
            Long userSeq,
            Pageable pageable
    ) {
        return postQueryDslRepository.getPostsByUserSeq(userSeq, pageable);
    }

    @Override
    public Optional<Post> getById(Long postSeq) {
        return postJpaRepository.findById(postSeq).map(PostEntity::toDomain);
    }

    @Override
    public Post save(Post post) {
        return postJpaRepository.save(PostEntity.from(post)).toDomain();
    }
}
