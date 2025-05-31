package com.backend.immilog.post.application.usecase;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.post.application.result.JobBoardResult;
import com.backend.immilog.post.application.services.JobBoardQueryService;
import com.backend.immilog.post.domain.model.post.Experience;
import com.backend.immilog.post.domain.model.post.Industry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

public interface JobBoardFetchUseCase {
    Page<JobBoardResult> getJobBoards(
            String country,
            String sortingMethod,
            String industry,
            String experience,
            Integer page
    );

    @Service
    class JobBoardFetcher implements JobBoardFetchUseCase {
        private final JobBoardQueryService jobBoardQueryService;

        public JobBoardFetcher(JobBoardQueryService jobBoardQueryService) {
            this.jobBoardQueryService = jobBoardQueryService;
        }

        @Transactional(readOnly = true)
        public Page<JobBoardResult> getJobBoards(
                String country,
                String sortingMethod,
                String industry,
                String experience,
                Integer page
        ) {
            var pageable = PageRequest.of(page, 10);
            var industryEnum = Industry.valueOf(industry);
            var countryEnum = Country.valueOf(country);
            var experienceEnum = Experience.valueOf(experience);
            return jobBoardQueryService.getJobBoards(countryEnum, sortingMethod, industryEnum, experienceEnum, pageable);
        }

    }
}

