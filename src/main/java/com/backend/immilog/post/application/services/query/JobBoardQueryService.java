package com.backend.immilog.post.application.services.query;

import com.backend.immilog.global.aop.monitor.PerformanceMonitor;
import com.backend.immilog.post.application.result.JobBoardResult;
import com.backend.immilog.post.domain.enums.Countries;
import com.backend.immilog.post.domain.enums.Experience;
import com.backend.immilog.post.domain.enums.Industry;
import com.backend.immilog.post.domain.enums.PostType;
import com.backend.immilog.post.domain.model.interaction.InteractionUser;
import com.backend.immilog.post.domain.model.post.JobBoard;
import com.backend.immilog.post.domain.model.resource.PostResource;
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
            Countries countryEnum,
            String sortingMethod,
            Industry industryEnum,
            Experience experienceEnum,
            Pageable pageable
    ) {
        Page<JobBoard> jobBoards = jobBoardRepository.getJobBoards(
                countryEnum,
                sortingMethod,
                industryEnum,
                experienceEnum,
                pageable
        );
        Page<JobBoardResult> jobBoardResults = jobBoards.map(JobBoard::toResult);
        return assembleJobBoardResult(jobBoards, jobBoardResults);
    }

    @Transactional(readOnly = true)
    public JobBoardResult getJobBoardBySeq(Long jobBoardSeq) {
        Page<JobBoard> jobBoard = new PageImpl<>(List.of(jobBoardRepository.getJobBoardBySeq(jobBoardSeq)));
        Page<JobBoardResult> jobBoards = jobBoard.map(JobBoard::toResult);
        return assembleJobBoardResult(jobBoard, jobBoards).getContent().getFirst();
    }

    private Page<JobBoardResult> assembleJobBoardResult(
            Page<JobBoard> jobBoards,
            Page<JobBoardResult> jobBoardResults
    ) {
        List<Long> jobBoardSeqList = jobBoards.stream().map(JobBoard::getSeq).toList();

        List<PostResource> postResources = postResourceQueryService.getResourcesByPostSeqList(
                jobBoardSeqList,
                PostType.JOB_BOARD
        );

        List<InteractionUser> interactionUsers = interactionUserQueryService.getInteractionUsersByPostSeqList(
                jobBoardSeqList,
                PostType.JOB_BOARD
        );

        return jobBoardResults.map(jobBoardResult -> {
            List<PostResource> resources = postResources.stream()
                    .filter(postResource -> postResource.getPostSeq().equals(jobBoardResult.getSeq()))
                    .toList();

            List<InteractionUser> interactionUserList = interactionUsers.stream()
                    .filter(interactionUser -> interactionUser.getPostSeq().equals(jobBoardResult.getSeq()))
                    .toList();

            jobBoardResult.addInteractionUsers(interactionUserList);
            jobBoardResult.addResources(resources);

            return jobBoardResult;
        });
    }
}
