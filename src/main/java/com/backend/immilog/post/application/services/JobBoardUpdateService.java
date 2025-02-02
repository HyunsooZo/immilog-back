package com.backend.immilog.post.application.services;

import com.backend.immilog.post.application.command.JobBoardUpdateCommand;
import com.backend.immilog.post.application.result.JobBoardResult;
import com.backend.immilog.post.application.services.command.BulkCommandService;
import com.backend.immilog.post.application.services.command.JobBoardCommandService;
import com.backend.immilog.post.application.services.command.PostResourceCommandService;
import com.backend.immilog.post.application.services.query.JobBoardQueryService;
import com.backend.immilog.post.domain.enums.PostType;
import com.backend.immilog.post.domain.enums.ResourceType;
import com.backend.immilog.post.domain.model.post.JobBoard;
import com.backend.immilog.post.domain.model.post.JobBoardCompany;
import com.backend.immilog.post.domain.model.post.PostInfo;
import com.backend.immilog.post.exception.PostException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.backend.immilog.post.exception.PostErrorCode.FAILED_TO_SAVE_POST;
import static com.backend.immilog.post.exception.PostErrorCode.NO_AUTHORITY;

@Slf4j
@Service
public class JobBoardUpdateService {
    private final JobBoardQueryService jobBoardQueryService;
    private final JobBoardCommandService jobBoardCommandService;
    private final PostResourceCommandService postResourceCommandService;
    private final BulkCommandService bulkInsertRepository;

    public JobBoardUpdateService(
            JobBoardQueryService jobBoardQueryService,
            JobBoardCommandService jobBoardCommandService,
            PostResourceCommandService postResourceCommandService,
            BulkCommandService bulkInsertRepository
    ) {
        this.jobBoardQueryService = jobBoardQueryService;
        this.jobBoardCommandService = jobBoardCommandService;
        this.postResourceCommandService = postResourceCommandService;
        this.bulkInsertRepository = bulkInsertRepository;
    }

    @Transactional
    public void updateJobBoard(
            Long userSeq,
            Long jobBoardSeq,
            JobBoardUpdateCommand command
    ) {
        JobBoardResult jobBoard = jobBoardQueryService.getJobBoardBySeq(jobBoardSeq);
        this.verifyOwner(userSeq, jobBoard);
        JobBoard updatedJobBoard = this.updatedJobBoard(command, jobBoard);
        jobBoardCommandService.save(updatedJobBoard);
    }

    @Transactional
    public void deactivateJobBoard(
            Long userSeq,
            Long jobBoardSeq
    ) {
        JobBoardResult jobBoardResult = jobBoardQueryService.getJobBoardBySeq(jobBoardSeq);
        this.verifyOwner(userSeq, jobBoardResult);
        JobBoard jobBoard = JobBoard.from(jobBoardResult);
        JobBoard updatedJobBoard = jobBoard.delete();
        jobBoardCommandService.save(updatedJobBoard);
    }


    private JobBoard updatedJobBoard(
            JobBoardUpdateCommand command,
            JobBoardResult jobBoard
    ) {
        PostInfo postInfo = new PostInfo(
                command.title() != null ? command.title() : jobBoard.getTitle(),
                command.content() != null ? command.content() : jobBoard.getContent(),
                jobBoard.getViewCount(),
                jobBoard.getLikeCount(),
                jobBoard.getRegion(),
                jobBoard.getStatus(),
                jobBoard.getCountry()
        );
        this.updateTags(command, jobBoard);
        this.updateAttachments(command, jobBoard);

        return new JobBoard(
                jobBoard.getSeq(),
                jobBoard.getCompanyManagerUserSeq(),
                postInfo,
                JobBoardCompany.of(
                        jobBoard.getCompanySeq(),
                        jobBoard.getIndustry(),
                        command.experience() != null ? command.experience() : jobBoard.getExperience(),
                        command.deadline() != null ? command.deadline() : jobBoard.getDeadline(),
                        command.salary() != null ? command.salary() : jobBoard.getSalary(),
                        jobBoard.getCompanyName(),
                        jobBoard.getCompanyEmail(),
                        jobBoard.getCompanyPhone(),
                        jobBoard.getCompanyAddress(),
                        jobBoard.getCompanyHomepage(),
                        jobBoard.getCompanyLogo()
                ),
                LocalDateTime.now(),
                null
        );
    }

    private void updateAttachments(
            JobBoardUpdateCommand command,
            JobBoardResult jobBoard
    ) {
        List<String> attachmentToDelete = jobBoard.getAttachments()
                .stream()
                .filter(attachment -> command.deleteAttachments().contains(attachment))
                .toList();

        postResourceCommandService.deleteAllEntities(
                jobBoard.getSeq(),
                PostType.JOB_BOARD,
                ResourceType.ATTACHMENT,
                attachmentToDelete
        );

        this.saveAllPostResources(
                jobBoard.getSeq(),
                ResourceType.ATTACHMENT,
                command.addAttachments()
        );
    }

    private void updateTags(
            JobBoardUpdateCommand command,
            JobBoardResult jobBoard
    ) {
        List<String> tagToDelete = jobBoard.getTags()
                .stream()
                .filter(tag -> command.deleteAttachments().contains(tag))
                .toList();

        postResourceCommandService.deleteAllEntities(
                jobBoard.getSeq(),
                PostType.JOB_BOARD,
                ResourceType.TAG,
                tagToDelete
        );

        this.saveAllPostResources(
                jobBoard.getSeq(),
                ResourceType.TAG,
                command.addTags()
        );
    }

    private void saveAllPostResources(
            Long postSeq,
            ResourceType resourceType,
            List<String> postResources
    ) {
        bulkInsertRepository.saveAll(
                postResources,
                """
                        INSERT INTO post_resource (
                            post_seq,
                            post_type,
                            resource_type,
                            content
                        ) VALUES (?, ?, ?, ?)
                        """,
                (ps, postResource) -> {
                    try {
                        ps.setLong(1, postSeq);
                        ps.setString(2, PostType.JOB_BOARD.name());
                        ps.setString(3, resourceType.name());
                        ps.setString(4, postResource);
                    } catch (SQLException e) {
                        log.error("Failed to save post resource: {}", e.getMessage());
                        throw new PostException(FAILED_TO_SAVE_POST);
                    }
                }
        );
    }

    private void verifyOwner(
            Long userSeq,
            JobBoardResult jobBoard
    ) {
        if (!Objects.equals(jobBoard.getCompanyManagerUserSeq(), userSeq)) {
            throw new PostException(NO_AUTHORITY);
        }
    }
}
