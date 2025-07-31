package com.backend.immilog.post.infrastructure.repositories;

import com.backend.immilog.post.domain.model.post.Categories;
import com.backend.immilog.post.domain.model.post.Post;
import com.backend.immilog.post.domain.model.post.SortingMethods;
import com.backend.immilog.post.domain.repositories.PostRepository;
import com.backend.immilog.post.exception.PostErrorCode;
import com.backend.immilog.post.exception.PostException;
import com.backend.immilog.post.infrastructure.jdbc.PostJdbcRepository;
import com.backend.immilog.post.infrastructure.jpa.entity.post.PostEntity;
import com.backend.immilog.post.infrastructure.jpa.repository.PostJpaRepository;
import com.backend.immilog.shared.enums.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.backend.immilog.post.exception.PostErrorCode.POST_NOT_FOUND;

@Repository
public class PostRepositoryImpl implements PostRepository {
    private final PostJdbcRepository postJdbcRepository;
    private final PostJpaRepository postJpaRepository;

    public PostRepositoryImpl(
            PostJdbcRepository postJdbcRepository,
            PostJpaRepository postJpaRepository
    ) {
        this.postJdbcRepository = postJdbcRepository;
        this.postJpaRepository = postJpaRepository;
    }

    @Override
    public Page<Post> getPosts(
            Country country,
            SortingMethods sortingMethod,
            String isPublic,
            Categories category,
            Pageable pageable
    ) {
        return postJdbcRepository.getPosts(
                country,
                sortingMethod,
                isPublic,
                category,
                pageable
        );
    }


    @Override
    public Post getPostDetail(String postId) {
        return postJdbcRepository
                .getSinglePost(postId)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));
    }

    @Override
    public Page<Post> getPostsByKeyword(
            String keyword,
            Pageable pageable
    ) {
        return postJdbcRepository.getPostsByKeyword(keyword, pageable);
    }

    @Override
    public Page<Post> getPostsByUserId(
            String userId,
            Pageable pageable
    ) {
        return postJdbcRepository.getPostsByUserId(userId, pageable);
    }

    @Override
    public Post getById(String postId) {
        return postJpaRepository
                .findById(postId)
                .map(PostEntity::toDomain)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));
    }

    @Override
    public Post save(Post post) {
        return postJpaRepository.save(PostEntity.from(post)).toDomain();
    }

    @Override
    public List<Post> getPostsByPostIdList(List<String> postIdList) {
        return postJpaRepository.findAllByIdIn(postIdList).stream()
                .map(PostEntity::toDomain)
                .toList();
    }
}
