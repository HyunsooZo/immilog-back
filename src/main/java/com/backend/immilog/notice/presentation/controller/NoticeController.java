package com.backend.immilog.notice.presentation.controller;

import com.backend.immilog.global.aop.ExtractUserId;
import com.backend.immilog.global.enums.UserRole;
import com.backend.immilog.notice.application.result.NoticeResult;
import com.backend.immilog.notice.application.services.NoticeCreateService;
import com.backend.immilog.notice.application.services.NoticeInquiryService;
import com.backend.immilog.notice.application.services.NoticeModifyService;
import com.backend.immilog.notice.presentation.request.NoticeModifyRequest;
import com.backend.immilog.notice.presentation.request.NoticeRegisterRequest;
import com.backend.immilog.notice.presentation.response.NoticeApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@Tag(name = "Notice API", description = "공지사항 관련 API")
@RequestMapping("/api/v1/notices")
@RequiredArgsConstructor
@RestController
public class NoticeController {
    private final NoticeCreateService noticeRegisterService;
    private final NoticeInquiryService noticeInquiryService;
    private final NoticeModifyService noticeModifyService;

    @PostMapping
    @ExtractUserId
    @Operation(summary = "공지사항 등록", description = "공지사항을 등록합니다.")
    public ResponseEntity<NoticeApiResponse> registerNotice(
            @RequestBody NoticeRegisterRequest noticeRegisterRequest,
            HttpServletRequest request
    ) {
        Long userSeq = (Long) request.getAttribute("userSeq");
        String userRole = ((UserRole) request.getAttribute("userRole")).name();
        noticeRegisterService.registerNotice(userSeq, userRole, noticeRegisterRequest.toCommand());
        return ResponseEntity.status(CREATED).body(NoticeApiResponse.of(true));
    }

    @GetMapping
    @ExtractUserId
    @Operation(summary = "공지사항 조회", description = "공지사항을 조회합니다.")
    public ResponseEntity<NoticeApiResponse> getNotices(
            @RequestParam("page") Integer page,
            HttpServletRequest request
    ) {
        Long userSeq = (Long) request.getAttribute("userSeq");
        Page<NoticeResult> notices = noticeInquiryService.getNotices(userSeq, page);
        return ResponseEntity.status(OK).body(NoticeApiResponse.of(notices));
    }

    @GetMapping("/{noticeSeq}")
    @Operation(summary = "공지사항 조회", description = "공지사항을 조회합니다.")
    public ResponseEntity<NoticeApiResponse> getNoticeDetail(
            @PathVariable("noticeSeq") Long noticeSeq
    ) {
        NoticeResult notices = noticeInquiryService.getNoticeDetail(noticeSeq);
        return ResponseEntity.status(OK).body(NoticeApiResponse.of(notices));
    }

    @GetMapping("/unread")
    @ExtractUserId
    @Operation(summary = "공지사항 존재 여부 조회", description = "공지사항이 존재하는지 여부를 조회합니다.")
    public ResponseEntity<NoticeApiResponse> isNoticeExist(
            HttpServletRequest request
    ) {
        Long userSeq = (Long) request.getAttribute("userSeq");
        Boolean unreadNoticeExist = noticeInquiryService.isUnreadNoticeExist(userSeq);
        return ResponseEntity.status(OK).body(NoticeApiResponse.of(unreadNoticeExist));
    }

    @PatchMapping("/{noticeSeq}")
    @ExtractUserId
    @Operation(summary = "공지사항 수정", description = "공지사항을 수정합니다.")
    public ResponseEntity<Void> modifyNotice(
            HttpServletRequest request,
            @PathVariable("noticeSeq") Long noticeSeq,
            @RequestBody NoticeModifyRequest param
    ) {
        Long userSeq = (Long) request.getAttribute("userSeq");
        noticeModifyService.modifyNotice(userSeq, noticeSeq, param.toCommand());
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @PatchMapping("/{noticeSeq}")
    @ExtractUserId
    @Operation(summary = "공지사항 읽음처리", description = "공지사항을 읽음 처리합니다.")
    public ResponseEntity<Void> readNotice(
            HttpServletRequest request,
            @PathVariable("noticeSeq") Long noticeSeq
    ) {
        Long userSeq = (Long) request.getAttribute("userSeq");
        noticeModifyService.readNotice(userSeq, noticeSeq);
        return ResponseEntity.status(NO_CONTENT).build();
    }


}