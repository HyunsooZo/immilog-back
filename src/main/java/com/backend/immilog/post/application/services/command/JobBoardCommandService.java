package com.backend.immilog.post.application.services.command;

import com.backend.immilog.post.domain.model.post.JobBoard;
import com.backend.immilog.post.domain.repositories.JobBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JobBoardCommandService {
    private final JobBoardRepository jobBoardRepository;

    @Transactional
    public void save(JobBoard updatedJobBoard) {
        jobBoardRepository.save(updatedJobBoard);
    }
}
