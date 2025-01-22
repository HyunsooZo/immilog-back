package com.backend.immilog.post.domain.repositories;

import com.backend.immilog.post.application.result.PostResult;
import com.backend.immilog.post.domain.enums.Categories;
import com.backend.immilog.post.domain.enums.Countries;
import com.backend.immilog.post.domain.enums.SortingMethods;
import com.backend.immilog.post.domain.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PostRepository {

    Page<PostResult> getPosts(
            Countries country,
            SortingMethods sortingMethod,
            String isPublic,
            Categories category,
            Pageable pageable
    );

    Optional<PostResult> getPost(Long postSeq);

    Page<PostResult> getPostsByKeyword(
            String keyword,
            Pageable pageable
    );

    Page<PostResult> getPostsByUserSeq(
            Long userSeq,
            Pageable pageable
    );

    Optional<Post> getById(Long postSeq);

    Post save(Post postEntity);

}