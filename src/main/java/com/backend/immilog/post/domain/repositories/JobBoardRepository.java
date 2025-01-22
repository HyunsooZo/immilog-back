package com.backend.immilog.post.domain.repositories;

import com.backend.immilog.post.application.result.JobBoardResult;
import com.backend.immilog.post.domain.enums.Countries;
import com.backend.immilog.post.domain.enums.Experience;
import com.backend.immilog.post.domain.enums.Industry;
import com.backend.immilog.post.domain.model.JobBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface JobBoardRepository {
    void save(JobBoard jobBoard);

    Page<JobBoardResult> getJobBoards(
            Countries country,
            String sortingMethod,
            Industry industry,
            Experience experience,
            Pageable pageable
    );

    Optional<JobBoardResult> getJobBoardBySeq(Long jobBoardSeq);
}
