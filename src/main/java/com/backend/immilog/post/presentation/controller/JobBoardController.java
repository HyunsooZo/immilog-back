package com.backend.immilog.post.presentation.controller;

import com.backend.immilog.global.aop.ExtractUserId;
import com.backend.immilog.post.application.result.JobBoardResult;
import com.backend.immilog.post.application.services.JobBoardInquiryService;
import com.backend.immilog.post.application.services.JobBoardUpdateService;
import com.backend.immilog.post.application.services.JobBoardUploadService;
import com.backend.immilog.post.presentation.request.JobBoardUpdateRequest;
import com.backend.immilog.post.presentation.request.JobBoardUploadRequest;
import com.backend.immilog.post.presentation.response.PostApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@Tag(name = "JobBoard API", description = "구인구직 업로드 관련 API")
@RequestMapping("/api/v1/job-boards")
@RequiredArgsConstructor
@RestController
public class JobBoardController {
    private final JobBoardUploadService jobBoardUploadService;
    private final JobBoardInquiryService jobBoardInquiryService;
    private final JobBoardUpdateService jobBoardUpdateService;

    @PostMapping
    @ExtractUserId
    @Operation(summary = "구인구직 게시글 업로드", description = "구인구직 게시글을 업로드합니다.")
    public ResponseEntity<PostApiResponse> uploadJobBoard(
            HttpServletRequest request,
            @RequestBody JobBoardUploadRequest jobBoardRequest
    ) {
        Long userSeq = (Long) request.getAttribute("userSeq");
        jobBoardUploadService.uploadJobBoard(userSeq, jobBoardRequest.toCommand());
        return ResponseEntity.status(CREATED).build();
    }

    @GetMapping
    @Operation(summary = "구인구직 게시글 조회", description = "구인구직 게시글을 조회합니다.")
    public ResponseEntity<PostApiResponse> searchJobBoard(
            @RequestParam(required = false, name = "country") String country,
            @RequestParam(required = false, name = "sortingMethod") String sortingMethod,
            @RequestParam(required = false, name = "industry") String industry,
            @RequestParam(required = false, name = "experience") String experience,
            @RequestParam(required = false, name = "page") Integer page
    ) {
        Page<JobBoardResult> jobBoards = jobBoardInquiryService.getJobBoards(
                country,
                sortingMethod,
                industry,
                experience,
                page
        );
        return ResponseEntity.status(OK).body(PostApiResponse.of(jobBoards));
    }

    @PatchMapping("/{jobBoardSeq}")
    @ExtractUserId
    @Operation(summary = "구인구직 게시글 수정", description = "구인구직 게시글을 수정합니다.")
    public ResponseEntity<Void> updateJobBoard(
            HttpServletRequest request,
            @PathVariable("jobBoardSeq") Long jobBoardSeq,
            @RequestBody JobBoardUpdateRequest jobBoardRequest
    ) {
        Long userSeq = (Long) request.getAttribute("userSeq");
        jobBoardUpdateService.updateJobBoard(
                userSeq,
                jobBoardSeq,
                jobBoardRequest.toCommand()
        );

        return ResponseEntity.status(NO_CONTENT).build();
    }

    @PatchMapping("/{jobBoardSeq}/delete")
    @ExtractUserId
    @Operation(summary = "구인구직 게시글 삭제", description = "구인구직 게시글을 삭제합니다.")
    public ResponseEntity<Void> deleteJobBoard(
            HttpServletRequest request,
            @PathVariable("jobBoardSeq") Long jobBoardSeq
    ) {
        Long userSeq = (Long) request.getAttribute("userSeq");
        jobBoardUpdateService.deactivateJobBoard(userSeq, jobBoardSeq);
        return ResponseEntity.status(NO_CONTENT).build();
    }

}
