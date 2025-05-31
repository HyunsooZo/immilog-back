package com.backend.immilog.post.application.services;

import com.backend.immilog.global.aop.monitor.PerformanceMonitor;
import com.backend.immilog.global.enums.Country;
import com.backend.immilog.post.application.result.JobBoardResult;
import com.backend.immilog.post.domain.model.post.Experience;
import com.backend.immilog.post.domain.model.post.Industry;
import com.backend.immilog.post.domain.model.post.JobBoard;
import com.backend.immilog.post.domain.model.post.PostType;
import com.backend.immilog.post.domain.repositories.JobBoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JobBoardQueryService {
    private final JobBoardRepository jobBoardRepository;
    private final InteractionUserQueryService interactionUserQueryService;
    private final PostResourceQueryService postResourceQueryService;

    public JobBoardQueryService(
            JobBoardRepository jobBoardRepository,
            InteractionUserQueryService interactionUserQueryService,
            PostResourceQueryService postResourceQueryService
    ) {
        this.jobBoardRepository = jobBoardRepository;
        this.interactionUserQueryService = interactionUserQueryService;
        this.postResourceQueryService = postResourceQueryService;
    }

    @PerformanceMonitor
    @Transactional(readOnly = true)
    public Page<JobBoardResult> getJobBoards(
            Country countryEnum,
            String sortingMethod,
            Industry industryEnum,
            Experience experienceEnum,
            Pageable pageable
    ) {
        var jobBoards = jobBoardRepository.getJobBoards(countryEnum, sortingMethod, industryEnum, experienceEnum, pageable);
        var jobBoardResults = jobBoards.map(JobBoard::toResult);
        return this.assembleJobBoardResult(jobBoards, jobBoardResults);
    }

    @Transactional(readOnly = true)
    public JobBoardResult getJobBoardBySeq(Long jobBoardSeq) {
        var jobBoard = new PageImpl<>(List.of(jobBoardRepository.getJobBoardBySeq(jobBoardSeq)));
        var jobBoards = jobBoard.map(JobBoard::toResult);
        return this.assembleJobBoardResult(jobBoard, jobBoards).getContent().getFirst();
    }

    private Page<JobBoardResult> assembleJobBoardResult(
            Page<JobBoard> jobBoards,
            Page<JobBoardResult> jobBoardResults
    ) {
        var jobBoardSeqList = jobBoards.stream().map(JobBoard::seq).toList();
        var postResources = postResourceQueryService.getResourcesByPostSeqList(jobBoardSeqList, PostType.JOB_BOARD);
        var interactionUsers = interactionUserQueryService.getInteractionUsersByPostSeqList(jobBoardSeqList, PostType.JOB_BOARD);

        return jobBoardResults.map(jobBoardResult -> {
            var resources = postResources.stream()
                    .filter(postResource -> postResource.postSeq().equals(jobBoardResult.getSeq()))
                    .toList();

            var interactionUserList = interactionUsers.stream()
                    .filter(interactionUser -> interactionUser.postSeq().equals(jobBoardResult.getSeq()))
                    .toList();

            jobBoardResult.addInteractionUsers(interactionUserList);
            jobBoardResult.addResources(resources);
            return jobBoardResult;
        });
    }
}
