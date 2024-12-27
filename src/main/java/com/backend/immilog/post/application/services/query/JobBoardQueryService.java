package com.backend.immilog.post.application.services.query;

import com.backend.immilog.global.aop.monitor.PerformanceMonitor;
import com.backend.immilog.post.application.result.JobBoardResult;
import com.backend.immilog.post.domain.enums.Countries;
import com.backend.immilog.post.domain.enums.Experience;
import com.backend.immilog.post.domain.enums.Industry;
import com.backend.immilog.post.domain.repositories.JobBoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class JobBoardQueryService {
    private final JobBoardRepository jobBoardRepository;

    public JobBoardQueryService(JobBoardRepository jobBoardRepository) {
        this.jobBoardRepository = jobBoardRepository;
    }

    @PerformanceMonitor
    @Transactional(readOnly = true)
    public Page<JobBoardResult> getJobBoards(
            Countries countryEnum,
            String sortingMethod,
            Industry industryEnum,
            Experience experienceEnum,
            Pageable pageable
    ) {
        return jobBoardRepository.getJobBoards(
                countryEnum,
                sortingMethod,
                industryEnum,
                experienceEnum,
                pageable
        );
    }

    @Transactional(readOnly = true)
    public Optional<JobBoardResult> getJobBoardBySeq(Long jobBoardSeq) {
        return jobBoardRepository.getJobBoardBySeq(jobBoardSeq);
    }
}
