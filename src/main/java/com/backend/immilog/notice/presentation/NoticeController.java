package com.backend.immilog.notice.presentation;

import com.backend.immilog.notice.application.dto.NoticeModelResult;
import com.backend.immilog.notice.application.usecase.NoticeCreateUseCase;
import com.backend.immilog.notice.application.usecase.NoticeFetchUseCase;
import com.backend.immilog.notice.application.usecase.NoticeModifyUseCase;
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
    private final NoticeCreateUseCase noticeRegisterService;
    private final NoticeFetchUseCase noticeFetchUseCase;
    private final NoticeModifyUseCase noticeModifyUseCase;

    public NoticeController(
            NoticeCreateUseCase noticeRegisterService,
            NoticeFetchUseCase noticeFetchUseCase,
            NoticeModifyUseCase noticeModifyUseCase
    ) {
        this.noticeRegisterService = noticeRegisterService;
        this.noticeFetchUseCase = noticeFetchUseCase;
        this.noticeModifyUseCase = noticeModifyUseCase;
    }

    @PostMapping
    @Operation(summary = "공지사항 등록", description = "공지사항을 등록합니다.")
    public ResponseEntity<NoticeRegistrationResponse> registerNotice(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestBody NoticeRegisterRequest noticeRegisterRequest
    ) {
        noticeRegisterService.createNotice(token, noticeRegisterRequest.toCommand());
        return ResponseEntity.status(HttpStatus.CREATED).body(NoticeRegistrationResponse.success());
    }

    @GetMapping("users/{userSeq}")
    @Operation(summary = "공지사항 조회", description = "공지사항을 조회합니다.")
    public ResponseEntity<NoticeListResponse> getNotices(
            @Parameter(description = "사용자 고유번호") @PathVariable("userSeq") Long userSeq,
            @Parameter(description = "페이지 번호") @RequestParam("page") Integer page
    ) {
        Page<NoticeModelResult> notices = noticeFetchUseCase.getNotices(userSeq, page);
        return ResponseEntity.status(HttpStatus.OK).body(NoticeListResponse.of(notices));
    }

    @GetMapping("/{noticeSeq}")
    @Operation(summary = "공지사항 상세 조회", description = "공지사항 상제정보를 조회합니다.")
    public ResponseEntity<NoticeDetailResponse> getNoticeDetail(
            @Parameter(description = "공지사항 고유번호") @PathVariable("noticeSeq") Long noticeSeq
    ) {
        NoticeModelResult notice = noticeFetchUseCase.getNoticeDetail(noticeSeq);
        return ResponseEntity.status(HttpStatus.OK).body(notice.toResponse());
    }

    @GetMapping("/users/{userSeq}/unread")
    @Operation(summary = "공지사항 존재 여부 조회", description = "공지사항이 존재하는지 여부를 조회합니다.")
    public ResponseEntity<NoticeRegistrationResponse> isNoticeExist(
            @Parameter(description = "사용자 고유번호") @PathVariable("userSeq") Long userSeq
    ) {
        Boolean unreadNoticeExist = noticeFetchUseCase.isUnreadNoticeExist(userSeq);
        return ResponseEntity.status(HttpStatus.OK).body(NoticeRegistrationResponse.of(unreadNoticeExist));
    }

    @PatchMapping("/{noticeSeq}")
    @Operation(summary = "공지사항 수정", description = "공지사항을 수정합니다.")
    public ResponseEntity<Void> modifyNotice(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @Parameter(description = "공지사항 고유번호") @PathVariable("noticeSeq") Long noticeSeq,
            @Parameter(description = "공지사항 수정바디") @RequestBody NoticeModifyRequest param
    ) {
        noticeModifyUseCase.modifyNotice(token, noticeSeq, param.toCommand());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{noticeSeq}/users/{userSeq}")
    @Operation(summary = "공지사항 읽음처리", description = "공지사항을 읽음 처리합니다.")
    public ResponseEntity<Void> readNotice(
            @Parameter(description = "공지사항 고유번호") @PathVariable("noticeSeq") Long noticeSeq,
            @Parameter(description = "사용자 고유번호") @PathVariable("userSeq") Long userSeq
    ) {
        noticeModifyUseCase.readNotice(userSeq, noticeSeq);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}