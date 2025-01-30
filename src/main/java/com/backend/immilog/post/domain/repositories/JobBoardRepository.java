package com.backend.immilog.post.domain.repositories;

import com.backend.immilog.post.domain.enums.Countries;
import com.backend.immilog.post.domain.enums.Experience;
import com.backend.immilog.post.domain.enums.Industry;
import com.backend.immilog.post.domain.model.post.JobBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobBoardRepository {
    void save(JobBoard jobBoard);

    Page<JobBoard> getJobBoards(
            Countries country,
            String sortingMethod,
            Industry industry,
            Experience experience,
            Pageable pageable
    );

    JobBoard getJobBoardBySeq(Long jobBoardSeq);
}
