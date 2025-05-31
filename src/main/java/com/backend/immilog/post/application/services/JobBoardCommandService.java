package com.backend.immilog.post.application.services;

import com.backend.immilog.post.domain.model.post.JobBoard;
import com.backend.immilog.post.domain.repositories.JobBoardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JobBoardCommandService {
    private final JobBoardRepository jobBoardRepository;

    public JobBoardCommandService(JobBoardRepository jobBoardRepository) {
        this.jobBoardRepository = jobBoardRepository;
    }

    @Transactional
    public void save(JobBoard updatedJobBoard) {
        jobBoardRepository.save(updatedJobBoard);
    }
}
