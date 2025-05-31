package com.backend.immilog.post.domain.repositories;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.post.domain.model.post.Experience;
import com.backend.immilog.post.domain.model.post.Industry;
import com.backend.immilog.post.domain.model.post.JobBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobBoardRepository {
    void save(JobBoard jobBoard);

    Page<JobBoard> getJobBoards(
            Country country,
            String sortingMethod,
            Industry industry,
            Experience experience,
            Pageable pageable
    );

    JobBoard getJobBoardBySeq(Long jobBoardSeq);
}
