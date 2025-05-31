package com.backend.immilog.post.application.usecase;

import com.backend.immilog.post.application.services.PostCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

public interface PopularPostFetchUseCase {
    void aggregatePopularPosts();

    @Slf4j
    @Service
    class PopularPostFetcher implements PopularPostFetchUseCase {
        private final PostCommandService postCommandService;
        private final PostFetchUseCase postFetchUseCase;

        public PopularPostFetcher(
                PostCommandService postCommandService,
                PostFetchUseCase postFetchUseCase
        ) {
            this.postCommandService = postCommandService;
            this.postFetchUseCase = postFetchUseCase;
        }

        public void aggregatePopularPosts() {
            var mostViewedPosts = postFetchUseCase.getMostViewedPosts();
            var hotPosts = postFetchUseCase.getHotPosts();

            try {
                int expiration = 60 * 60;
                if (mostViewedPosts.isEmpty() && hotPosts.isEmpty()) {
                    log.warn("[POPULAR POST AGGREGATION FAILED] Popular posts are empty");
                    return;
                }
                postCommandService.saveMostViewedPosts(mostViewedPosts, expiration);
                postCommandService.saveHotPosts(hotPosts, expiration);
            } catch (Exception e) {
                log.error("[POPULAR POST AGGREGATION FAILED] Failed to save popular posts", e);
            }
        }
    }
}

