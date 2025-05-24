package com.backend.immilog.user.presentation.controller;

import com.backend.immilog.user.application.usecase.impl.*;
import com.backend.immilog.user.presentation.payload.UserGeneralResponse;
import com.backend.immilog.user.presentation.payload.UserInformationPayload;
import com.backend.immilog.user.presentation.payload.UserSignInPayload;
import com.backend.immilog.user.presentation.payload.UserSignUpPayload;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.backend.immilog.user.presentation.controller.EmailComponents.*;
import static org.springframework.http.HttpStatus.*;

@Tag(name = "User API", description = "사용자 관련 API")
@RequestMapping("/api/v1/users")
@RestController
public class UserController {
    private final UserSignUpService userSignUpService;
    private final UserSignInService userSignInService;
    private final UserUpdateService userUpdateService;
    private final UserReportService userReportService;
    private final LocationFetchingService locationFetchingService;
    private final EmailSendingService emailSendingService;

    public UserController(
            UserSignUpService userSignUpService,
            UserSignInService userSignInService,
            UserUpdateService userUpdateService,
            UserReportService userReportService,
            LocationFetchingService locationFetchingService,
            EmailSendingService emailSendingService
    ) {
        this.userSignUpService = userSignUpService;
        this.userSignInService = userSignInService;
        this.userUpdateService = userUpdateService;
        this.userReportService = userReportService;
        this.locationFetchingService = locationFetchingService;
        this.emailSendingService = emailSendingService;
    }

    @PostMapping
    @Operation(summary = "사용자 회원가입", description = "사용자 회원가입 진행")
    public ResponseEntity<UserGeneralResponse> signUp(
            @Valid @RequestBody UserSignUpPayload.UserSignUpRequest request
    ) {
        final var userSeqAndName = userSignUpService.signUp(request.toCommand());
        final var email = request.email();
        final var userName = userSeqAndName.getSecond();
        final var userSeq = userSeqAndName.getFirst();
        final var url = String.format(API_LINK, userSeq);
        final var mailForm = String.format(HTML_SIGN_UP_CONTENT, userName, url);
        emailSendingService.sendHtmlEmail(email, EMAIL_SIGN_UP_SUBJECT, mailForm);

        return ResponseEntity.status(CREATED).build();
    }

    @PostMapping("/sign-in")
    @Operation(summary = "사용자 로그인", description = "사용자 로그인 진행")
    public ResponseEntity<UserSignInPayload.UserSignInResponse> signIn(
            @Valid @RequestBody UserSignInPayload.UserSignInRequest request
    ) {
        var country = locationFetchingService.getCountry(request.latitude(), request.longitude());
        final var userSignInResult = userSignInService.signIn(request.toCommand(), country);
        return ResponseEntity.status(OK).body(userSignInResult.toResponse());
    }

    @PatchMapping("/{userSeq}/information")
    @Operation(summary = "사용자 정보 수정", description = "사용자 정보 수정 진행")
    public ResponseEntity<UserGeneralResponse> updateInformation(
            @Parameter(description = "사용자 고유번호") @PathVariable("userSeq") Long userSeq,
            @RequestBody UserInformationPayload.UserInfoUpdateRequest request
    ) {
        var country = locationFetchingService.getCountry(request.latitude(), request.longitude());
        userUpdateService.updateInformation(userSeq, country, request.toCommand());
        return ResponseEntity.status(OK).body(UserGeneralResponse.success());
    }

    @PatchMapping("/{userSeq}/password/change")
    @Operation(summary = "비밀번호 변경", description = "비밀번호 변경 진행")
    public ResponseEntity<Void> changePassword(
            @Parameter(description = "사용자 고유번호") @PathVariable("userSeq") Long userSeq,
            @RequestBody UserInformationPayload.UserPasswordChangeRequest request
    ) {
        userUpdateService.changePassword(userSeq, request.toCommand());
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @GetMapping("/nicknames")
    @Operation(summary = "닉네임 중복 체크", description = "닉네임 중복 체크 진행")
    public ResponseEntity<UserInformationPayload.UserNicknameResponse> checkNickname(
            @Parameter(description = "닉네임") @RequestParam("nickname") String nickname
    ) {
        var isNicknameAvailable = userSignUpService.isNicknameAvailable(nickname);
        return ResponseEntity.status(OK).body(new UserInformationPayload.UserNicknameResponse(isNicknameAvailable));
    }

    @PatchMapping("/{userSeq}/targets/{targetSeq}/{status}")
    @Operation(summary = "사용자 차단/해제", description = "사용자 차단/해제 진행")
    public ResponseEntity<Void> blockUser(
            @Parameter(description = "사용자 고유번호") @PathVariable("userSeq") Long userSeq,
            @Parameter(description = "대상 사용자 고유번호") @PathVariable("targetSeq") Long targetSeq,
            @Parameter(description = "상태") @PathVariable("status") String status
    ) {
        userUpdateService.blockOrUnblockUser(targetSeq, userSeq, status);
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @PatchMapping("/{userSeq}/targets/{targetSeq}/report")
    @Operation(summary = "사용자 신고", description = "사용자 신고 진행")
    public ResponseEntity<Void> reportUser(
            @Parameter(description = "사용자 고유번호") @PathVariable("userSeq") Long userSeq,
            @Parameter(description = "대상 사용자 고유번호") @PathVariable("targetSeq") Long targetSeq,
            @Valid @RequestBody UserInformationPayload.UserReportRequest request
    ) {
        userReportService.reportUser(targetSeq, userSeq, request.toCommand());
        return ResponseEntity.status(NO_CONTENT).build();
    }

}
