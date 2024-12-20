package com.backend.immilog.post.domain.repositories;

import com.backend.immilog.post.application.result.PostResult;
import com.backend.immilog.post.domain.model.post.Post;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.time.LocalDateTime;
import java.util.List;

public interface PopularPostRepository {
    void saveMostViewedPosts(
            List<PostResult> posts,
            Integer expiration
    ) throws JsonProcessingException;

    void saveHotPosts(
            List<PostResult> popularPosts,
            int expiration
    ) throws JsonProcessingException;


    List<PostResult> getMostViewedPosts(
            LocalDateTime from,
            LocalDateTime to
    );

    List<PostResult> getHotPosts(
            LocalDateTime from,
            LocalDateTime to
    );
}
