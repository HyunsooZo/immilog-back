package com.backend.immilog.post.application.services;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.post.application.result.JobBoardResult;
import com.backend.immilog.post.application.usecase.JobBoardFetchUseCase;
import com.backend.immilog.post.domain.model.post.Experience;
import com.backend.immilog.post.domain.model.post.Industry;
import com.backend.immilog.post.domain.model.post.PostStatus;
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
class JobBoardFetchUseCaseTest {
    private final JobBoardQueryService jobBoardQueryService = mock(JobBoardQueryService.class);
    private final JobBoardFetchUseCase jobBoardFetchUseCase = new JobBoardFetchUseCase.JobBoardFetcher(jobBoardQueryService);

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
        JobBoardResult jobBoardResult = new JobBoardResult(
                1L,
                "title",
                "content",
                0L,
                0L,
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Country.SOUTH_KOREA,
                "region",
                Industry.IT,
                now,
                Experience.JUNIOR,
                "salary",
                1L,
                "name",
                "email",
                "phone",
                "address",
                "homepage",
                "logo",
                1L,
                PostStatus.NORMAL,
                now
        );

        when(jobBoardQueryService.getJobBoards(
                Country.valueOf(country),
                sortingMethod,
                Industry.valueOf(industry),
                Experience.valueOf(experience),
                pageable
        )).thenReturn(new PageImpl<>(List.of(jobBoardResult)));

        // when
        Page<JobBoardResult> jobBoards = jobBoardFetchUseCase.getJobBoards(country, sortingMethod, industry, experience, page);

        // then
        JobBoardResult result = jobBoards.getContent().getFirst();
        assertThat(result.getSeq()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("title");
    }
}