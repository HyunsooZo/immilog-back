package com.backend.immilog.post.application.result;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.post.domain.model.post.Experience;
import com.backend.immilog.post.domain.model.post.Industry;
import com.backend.immilog.post.domain.model.post.PostStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record JobBoardResult(
        Long seq,
        String title,
        String content,
        Long viewCount,
        Long likeCount,
        List<String> tags,
        List<String> attachments,
        List<Long> likeUsers,
        List<Long> bookmarkUsers,
        Country country,
        String region,
        Industry industry,
        LocalDateTime deadline,
        Experience experience,
        String salary,
        Long companySeq,
        String companyName,
        String companyEmail,
        String companyPhone,
        String companyAddress,
        String companyHomepage,
        String companyLogo,
        Long companyManagerUserSeq,
        PostStatus status,
        LocalDateTime createdAt
) {
    public JobBoardResult(
            Long seq,
            String title,
            String content,
            Long viewCount,
            Long likeCount,
            String region,
            PostStatus status,
            Country country,
            Industry industry,
            Experience experience,
            LocalDateTime deadline,
            String salary,
            Long companySeq,
            String companyName,
            String companyEmail,
            String companyPhone,
            String companyAddress,
            String companyHomepage,
            String companyLogo,
            Long companyManagerUserSeq,
            LocalDateTime createdAt
    ) {
        this(
                seq,
                title,
                content,
                viewCount,
                likeCount,
                new ArrayList<>(), // tags
                new ArrayList<>(), // attachments
                new ArrayList<>(), // likeUsers
                new ArrayList<>(), // bookmarkUsers
                country,
                region,
                industry,
                deadline,
                experience,
                salary,
                companySeq,
                companyName,
                companyEmail,
                companyPhone,
                companyAddress,
                companyHomepage,
                companyLogo,
                companyManagerUserSeq,
                status,
                createdAt
        );
    }
}
