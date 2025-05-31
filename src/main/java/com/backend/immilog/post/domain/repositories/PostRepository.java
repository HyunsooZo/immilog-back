package com.backend.immilog.post.domain.repositories;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.post.domain.model.post.Categories;
import com.backend.immilog.post.domain.model.post.Post;
import com.backend.immilog.post.domain.model.post.SortingMethods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepository {

    Page<Post> getPosts(
            Country country,
            SortingMethods sortingMethod,
            String isPublic,
            Categories category,
            Pageable pageable
    );

    Post getPostDetail(Long postSeq);

    Page<Post> getPostsByKeyword(
            String keyword,
            Pageable pageable
    );

    Page<Post> getPostsByUserSeq(
            Long userSeq,
            Pageable pageable
    );

    Post getById(Long postSeq);

    Post save(Post postEntity);

    List<Post> getPostsByPostSeqList(List<Long> postSeqList);
}