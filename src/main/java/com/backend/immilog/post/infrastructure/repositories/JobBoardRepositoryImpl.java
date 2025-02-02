package com.backend.immilog.post.infrastructure.repositories;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.post.domain.enums.Experience;
import com.backend.immilog.post.domain.enums.Industry;
import com.backend.immilog.post.domain.model.post.JobBoard;
import com.backend.immilog.post.domain.repositories.JobBoardRepository;
import com.backend.immilog.post.exception.PostErrorCode;
import com.backend.immilog.post.exception.PostException;
import com.backend.immilog.post.infrastructure.jdbc.JobBoardJdbcRepository;
import com.backend.immilog.post.infrastructure.jpa.entity.post.JobBoardEntity;
import com.backend.immilog.post.infrastructure.jpa.repository.JobBoardJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JobBoardRepositoryImpl implements JobBoardRepository {
    private final JobBoardJpaRepository jobBoardJpaRepository;
    private final JobBoardJdbcRepository jobBoardJdbcRepository;

    public JobBoardRepositoryImpl(
            JobBoardJpaRepository jobBoardJpaRepository,
            JobBoardJdbcRepository jobBoardJdbcRepository
    ) {
        this.jobBoardJpaRepository = jobBoardJpaRepository;
        this.jobBoardJdbcRepository = jobBoardJdbcRepository;
    }

    @Override
    public void save(JobBoard jobBoard) {
        jobBoardJpaRepository.save(JobBoardEntity.from(jobBoard));
    }

    @Override
    public Page<JobBoard> getJobBoards(
            Country country,
            String sortingMethod,
            Industry industry,
            Experience experience,
            Pageable pageable
    ) {
        List<JobBoardEntity> entities = jobBoardJdbcRepository.getJobBoards(country, sortingMethod, industry, experience, pageable);
        Integer total = jobBoardJdbcRepository.getTotal(country, industry, experience);
        List<JobBoard> jobBoards = entities.stream().map(JobBoardEntity::toDomain).toList();
        return new PageImpl<>(jobBoards, pageable, total);
    }

    @Override
    public JobBoard getJobBoardBySeq(Long jobBoardSeq) {
        return  jobBoardJpaRepository.findById(jobBoardSeq)
                .orElseThrow(() -> new PostException(PostErrorCode.JOB_BOARD_NOT_FOUND))
                .toDomain();
    }
}