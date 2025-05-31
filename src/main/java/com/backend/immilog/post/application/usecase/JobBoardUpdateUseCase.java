package com.backend.immilog.post.application.usecase;

import com.backend.immilog.post.application.command.JobBoardUpdateCommand;
import com.backend.immilog.post.application.result.JobBoardResult;
import com.backend.immilog.post.application.services.BulkCommandService;
import com.backend.immilog.post.application.services.JobBoardCommandService;
import com.backend.immilog.post.application.services.JobBoardQueryService;
import com.backend.immilog.post.application.services.PostResourceCommandService;
import com.backend.immilog.post.domain.model.post.JobBoard;
import com.backend.immilog.post.domain.model.post.JobBoardCompany;
import com.backend.immilog.post.domain.model.post.PostInfo;
import com.backend.immilog.post.domain.model.post.PostType;
import com.backend.immilog.post.domain.model.resource.ResourceType;
import com.backend.immilog.post.domain.service.JobBoardValidator;
import com.backend.immilog.post.exception.PostException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static com.backend.immilog.post.exception.PostErrorCode.FAILED_TO_SAVE_POST;

public interface JobBoardUpdateUseCase {
    void updateJobBoard(
            Long userSeq,
            Long jobBoardSeq,
            JobBoardUpdateCommand command
    );

    void deactivateJobBoard(
            Long userSeq,
            Long jobBoardSeq
    );

    @Slf4j
    @Service
    class JobBoardUpdater implements JobBoardUpdateUseCase {
        private final JobBoardQueryService jobBoardQueryService;
        private final JobBoardCommandService jobBoardCommandService;
        private final PostResourceCommandService postResourceCommandService;
        private final BulkCommandService bulkInsertRepository;
        private final JobBoardValidator jobBoardValidator;

        public JobBoardUpdater(
                JobBoardQueryService jobBoardQueryService,
                JobBoardCommandService jobBoardCommandService,
                PostResourceCommandService postResourceCommandService,
                BulkCommandService bulkInsertRepository,
                JobBoardValidator jobBoardValidator
        ) {
            this.jobBoardQueryService = jobBoardQueryService;
            this.jobBoardCommandService = jobBoardCommandService;
            this.postResourceCommandService = postResourceCommandService;
            this.bulkInsertRepository = bulkInsertRepository;
            this.jobBoardValidator = jobBoardValidator;
        }

        @Transactional
        public void updateJobBoard(
                Long userSeq,
                Long jobBoardSeq,
                JobBoardUpdateCommand command
        ) {
            var jobBoard = jobBoardQueryService.getJobBoardBySeq(jobBoardSeq);
            jobBoardValidator.validateOwner(userSeq, jobBoard.getCompanyManagerUserSeq());
            var updatedJobBoard = this.updatedJobBoard(command, jobBoard);
            jobBoardCommandService.save(updatedJobBoard);
        }

        @Transactional
        public void deactivateJobBoard(
                Long userSeq,
                Long jobBoardSeq
        ) {
            var jobBoardResult = jobBoardQueryService.getJobBoardBySeq(jobBoardSeq);
            jobBoardValidator.validateOwner(userSeq, jobBoardResult.getCompanyManagerUserSeq());
            var jobBoard = JobBoard.from(jobBoardResult);
            jobBoardCommandService.save(jobBoard.delete());
        }


        private JobBoard updatedJobBoard(
                JobBoardUpdateCommand command,
                JobBoardResult jobBoard
        ) {
            var postInfo = new PostInfo(
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
            var attachmentToDelete = jobBoard.getAttachments()
                    .stream()
                    .filter(attachment -> command.deleteAttachments().contains(attachment))
                    .toList();

            postResourceCommandService.deleteAllEntities(jobBoard.getSeq(), PostType.JOB_BOARD, ResourceType.ATTACHMENT, attachmentToDelete);

            this.saveAllPostResources(jobBoard.getSeq(), ResourceType.ATTACHMENT, command.addAttachments());
        }

        private void updateTags(
                JobBoardUpdateCommand command,
                JobBoardResult jobBoard
        ) {
            var tagToDelete = jobBoard.getTags().stream().filter(tag -> command.deleteAttachments().contains(tag)).toList();
            postResourceCommandService.deleteAllEntities(jobBoard.getSeq(), PostType.JOB_BOARD, ResourceType.TAG, tagToDelete);
            this.saveAllPostResources(jobBoard.getSeq(), ResourceType.TAG, command.addTags());
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

    }
}
