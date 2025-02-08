package com.backend.immilog.notice.presentation.controller;

import com.backend.immilog.notice.application.result.NoticeResult;
import com.backend.immilog.notice.application.services.NoticeCreateService;
import com.backend.immilog.notice.application.services.NoticeInquiryService;
import com.backend.immilog.notice.application.services.NoticeModifyService;
import com.backend.immilog.notice.presentation.request.NoticeModifyRequest;
import com.backend.immilog.notice.presentation.request.NoticeRegisterRequest;
import com.backend.immilog.notice.presentation.response.NoticeDetailResponse;
import com.backend.immilog.notice.presentation.response.NoticeListResponse;
import com.backend.immilog.notice.presentation.response.NoticeRegistrationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Notice API", description = "공지사항 관련 API")
@RequestMapping("/api/v1/notices")
@RestController
public class NoticeController {
    private final NoticeCreateService noticeRegisterService;
    private final NoticeInquiryService noticeInquiryService;
    private final NoticeModifyService noticeModifyService;

    public NoticeController(
            NoticeCreateService noticeRegisterService,
            NoticeInquiryService noticeInquiryService,
            NoticeModifyService noticeModifyService
    ) {
        this.noticeRegisterService = noticeRegisterService;
        this.noticeInquiryService = noticeInquiryService;
        this.noticeModifyService = noticeModifyService;
    }

    @PostMapping
    @Operation(summary = "공지사항 등록", description = "공지사항을 등록합니다.")
    public ResponseEntity<NoticeRegistrationResponse> registerNotice(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestBody NoticeRegisterRequest noticeRegisterRequest
    ) {
        noticeRegisterService.registerNotice(token, noticeRegisterRequest.toCommand());
        return ResponseEntity.status(HttpStatus.CREATED).body(NoticeRegistrationResponse.success());
    }

    @GetMapping("users/{userSeq}")
    @Operation(summary = "공지사항 조회", description = "공지사항을 조회합니다.")
    public ResponseEntity<NoticeListResponse> getNotices(
            @Parameter(description = "사용자 고유번호") @PathVariable("userSeq") Long userSeq,
            @Parameter(description = "페이지 번호") @RequestParam("page") Integer page
    ) {
        Page<NoticeResult> notices = noticeInquiryService.getNotices(userSeq, page);
        return ResponseEntity.status(HttpStatus.OK).body(NoticeListResponse.of(notices));
    }

    @GetMapping("/{noticeSeq}")
    @Operation(summary = "공지사항 상세 조회", description = "공지사항 상제정보를 조회합니다.")
    public ResponseEntity<NoticeDetailResponse> getNoticeDetail(
            @Parameter(description = "공지사항 고유번호") @PathVariable("noticeSeq") Long noticeSeq
    ) {
        NoticeResult notice = noticeInquiryService.getNoticeDetail(noticeSeq);
        return ResponseEntity.status(HttpStatus.OK).body(notice.toResponse());
    }

    @GetMapping("/users/{userSeq}/unread")
    @Operation(summary = "공지사항 존재 여부 조회", description = "공지사항이 존재하는지 여부를 조회합니다.")
    public ResponseEntity<NoticeRegistrationResponse> isNoticeExist(
            @Parameter(description = "사용자 고유번호") @PathVariable("userSeq") Long userSeq
    ) {
        Boolean unreadNoticeExist = noticeInquiryService.isUnreadNoticeExist(userSeq);
        return ResponseEntity.status(HttpStatus.OK).body(NoticeRegistrationResponse.of(unreadNoticeExist));
    }

    @PatchMapping("/{noticeSeq}")
    @Operation(summary = "공지사항 수정", description = "공지사항을 수정합니다.")
    public ResponseEntity<Void> modifyNotice(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @Parameter(description = "공지사항 고유번호") @PathVariable("noticeSeq") Long noticeSeq,
            @Parameter(description = "공지사항 수정바디") @RequestBody NoticeModifyRequest param
    ) {
        noticeModifyService.modifyNotice(token, noticeSeq, param.toCommand());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{noticeSeq}/users/{userSeq}")
    @Operation(summary = "공지사항 읽음처리", description = "공지사항을 읽음 처리합니다.")
    public ResponseEntity<Void> readNotice(
            @Parameter(description = "공지사항 고유번호") @PathVariable("noticeSeq") Long noticeSeq,
            @Parameter(description = "사용자 고유번호") @PathVariable("userSeq") Long userSeq
    ) {
        noticeModifyService.readNotice(userSeq, noticeSeq);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}