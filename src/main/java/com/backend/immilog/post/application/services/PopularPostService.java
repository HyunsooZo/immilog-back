package com.backend.immilog.post.application.services;

import com.backend.immilog.post.application.result.PostResult;
import com.backend.immilog.post.application.services.command.PostCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PopularPostService {
    private final PostCommandService postCommandService;
    private final PostInquiryService postInquiryService;

    public PopularPostService(
            PostCommandService postCommandService,
            PostInquiryService postInquiryService
    ) {
        this.postCommandService = postCommandService;
        this.postInquiryService = postInquiryService;
    }

    public void aggregatePopularPosts() {
        List<PostResult> mostViewedPosts = postInquiryService.getMostViewedPosts();
        List<PostResult> hotPosts = postInquiryService.getHotPosts();

        try {
            int expiration = 60 * 60;
            boolean isMostViewedPostsEmpty = mostViewedPosts.isEmpty();
            boolean isHotPostsEmpty = hotPosts.isEmpty();

            if (isMostViewedPostsEmpty && isHotPostsEmpty) {
                log.error("[POPULAR POST AGGREGATION FAILED] Popular posts are empty");
                return;
            }

            if (isMostViewedPostsEmpty) {
                log.error("[POPULAR POST AGGREGATION FAILED] Failed to save most viewed posts");
            }

            if (isHotPostsEmpty) {
                log.error("[POPULAR POST AGGREGATION FAILED] Failed to save hot posts");
            }

            postCommandService.saveMostViewedPosts(mostViewedPosts, expiration);
            postCommandService.saveHotPosts(hotPosts, expiration);
        } catch (Exception e) {
            log.error("[POPULAR POST AGGREGATION FAILED] Failed to save popular posts", e);
        }
    }
}
