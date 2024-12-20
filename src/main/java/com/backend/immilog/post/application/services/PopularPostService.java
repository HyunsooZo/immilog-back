package com.backend.immilog.post.application.services;

import com.backend.immilog.post.application.result.PostResult;
import com.backend.immilog.post.application.services.command.PostCommandService;
import com.backend.immilog.post.application.services.query.PostQueryService;
import com.backend.immilog.post.domain.model.post.Post;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class PopularPostService {
    private final PostInquiryService postQueryService;
    private final PostCommandService postCommandService;
    private final ObjectMapper objectMapper;
    private final PostInquiryService postInquiryService;

    public PopularPostService(
            PostInquiryService postQueryService,
            PostCommandService postCommandService,
            ObjectMapper objectMapper,
            PostInquiryService postInquiryService
    ) {
        this.postQueryService = postQueryService;
        this.postCommandService = postCommandService;
        this.objectMapper = objectMapper;
        this.postInquiryService = postInquiryService;
    }

    public void aggregatePopularPosts() {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        LocalDateTime now = LocalDateTime.now();

        List<PostResult> mostViewedPosts = postInquiryService.getMostViewedPosts();
        List<PostResult> hotPosts = postInquiryService.getHotPosts();

        int expiration = 60 * 60;
        try {
            postCommandService.saveMostViewedPosts(mostViewedPosts, expiration);
            postCommandService.saveHotPosts(hotPosts, expiration);
        } catch (Exception e) {
            log.error("Failed to save popular posts", e);
        }
    }
}
