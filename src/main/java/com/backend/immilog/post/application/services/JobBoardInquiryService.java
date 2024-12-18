package com.backend.immilog.post.application.services;

import com.backend.immilog.post.application.result.JobBoardResult;
import com.backend.immilog.post.application.services.query.JobBoardQueryService;
import com.backend.immilog.post.domain.enums.Countries;
import com.backend.immilog.post.domain.enums.Experience;
import com.backend.immilog.post.domain.enums.Industry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JobBoardInquiryService {
    private final JobBoardQueryService jobBoardQueryService;

    public JobBoardInquiryService(JobBoardQueryService jobBoardQueryService) {
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
        Pageable pageable = PageRequest.of(page, 10);
        Industry industryEnum = Industry.valueOf(industry);
        Countries countryEnum = Countries.valueOf(country);
        Experience experienceEnum = Experience.valueOf(experience);

        return jobBoardQueryService.getJobBoards(
                countryEnum,
                sortingMethod,
                industryEnum,
                experienceEnum,
                pageable
        );
    }

}
