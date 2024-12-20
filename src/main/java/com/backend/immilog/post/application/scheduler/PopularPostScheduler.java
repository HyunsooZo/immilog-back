package com.backend.immilog.post.application.scheduler;

import com.backend.immilog.post.application.services.PopularPostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PopularPostScheduler {
    private final PopularPostService popularPostService;

    PopularPostScheduler(PopularPostService popularPostService) {
        this.popularPostService = popularPostService;
    }

    @Scheduled(cron = "0 0 * * * *")
    public void aggregatePopularPosts() {
        log.info("[POPULAR POST AGGREGATION] Started aggregating popular posts...");
        popularPostService.aggregatePopularPosts();
        log.info("[POPULAR POST AGGREGATION] Finished aggregating popular posts.");
    }
}