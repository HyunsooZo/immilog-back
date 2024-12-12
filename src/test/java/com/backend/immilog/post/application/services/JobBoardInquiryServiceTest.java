package com.backend.immilog.post.application.services;

import com.backend.immilog.post.application.result.JobBoardResult;
import com.backend.immilog.post.application.services.query.JobBoardQueryService;
import com.backend.immilog.post.domain.enums.Countries;
import com.backend.immilog.post.domain.enums.Experience;
import com.backend.immilog.post.domain.enums.Industry;
import com.backend.immilog.post.domain.enums.PostStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("JobBoardInquiryService 테스트")
class JobBoardInquiryServiceTest {
    private final JobBoardQueryService jobBoardQueryService = mock(JobBoardQueryService.class);
    private final JobBoardInquiryService jobBoardInquiryService = new JobBoardInquiryService(jobBoardQueryService);

    @Test
    @DisplayName("구인구직 게시글 조회 성공")
    void getJobBoards() {
        // given
        String country = "SOUTH_KOREA";
        String sortingMethod = "CREATED_DATE";
        String industry = "IT";
        String experience = "JUNIOR";
        int page = 0;
        Pageable pageable = PageRequest.of(page, 10);
        LocalDateTime now = LocalDateTime.now();
        JobBoardResult jobBoardResult = JobBoardResult.builder()
                .seq(1L)
                .title("title")
                .content("content")
                .viewCount(0L)
                .likeCount(0L)
                .tags(Collections.emptyList())
                .attachments(Collections.emptyList())
                .likeUsers(Collections.emptyList())
                .bookmarkUsers(Collections.emptyList())
                .country(Countries.SOUTH_KOREA)
                .region("region")
                .industry(com.backend.immilog.user.domain.enums.Industry.IT)
                .deadline(now)
                .experience(Experience.JUNIOR)
                .salary("salary")
                .companyName("companyName")
                .companyEmail("companyEmail")
                .companyPhone("companyPhone")
                .companyAddress("companyAddress")
                .companyHomepage("companyHomepage")
                .companyLogo("companyLogo")
                .companyManagerUserSeq(1L)
                .status(PostStatus.NORMAL)
                .createdAt(now)
                .build();

        when(jobBoardQueryService.getJobBoards(
                Countries.valueOf(country),
                sortingMethod,
                Industry.valueOf(industry),
                Experience.valueOf(experience),
                pageable
        )).thenReturn(new PageImpl<>(List.of(jobBoardResult)));

        // when
        Page<JobBoardResult> jobBoards = jobBoardInquiryService.getJobBoards(country, sortingMethod, industry, experience, page);

        // then
        JobBoardResult result = jobBoards.getContent().getFirst();
        assertThat(result.seq()).isEqualTo(1L);
        assertThat(result.title()).isEqualTo("title");
    }
}