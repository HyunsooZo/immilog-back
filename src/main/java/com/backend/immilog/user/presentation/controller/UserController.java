package com.backend.immilog.user.presentation.controller;

import com.backend.immilog.global.aop.ExtractUserId;
import com.backend.immilog.user.application.result.UserSignInResult;
import com.backend.immilog.user.application.services.*;
import com.backend.immilog.user.domain.enums.UserStatus;
import com.backend.immilog.user.presentation.request.*;
import com.backend.immilog.user.presentation.response.UserApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

import static com.backend.immilog.user.enums.EmailComponents.*;
import static org.springframework.http.HttpStatus.*;

@Tag(name = "User API", description = "사용자 관련 API")
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserSignUpService userSignUpService;
    private final UserSignInService userSignInService;
    private final UserInformationService userInformationService;
    private final UserReportService userReportService;

    private final LocationService locationService;
    private final EmailService emailService;

    @PostMapping
    @Operation(summary = "사용자 회원가입", description = "사용자 회원가입 진행")
    public ResponseEntity<UserApiResponse> signUp(
            @Valid @RequestBody UserSignUpRequest request
    ) {
        final Pair<Long, String> userSeqAndName = userSignUpService.signUp(request.toCommand());
        final String email = request.email();
        final String userName = userSeqAndName.getSecond();
        final Long userSeq = userSeqAndName.getFirst();
        final String url = String.format(API_LINK, userSeq);
        final String mailForm = String.format(HTML_SIGN_UP_CONTENT, userName, url);
        emailService.sendHtmlEmail(email, EMAIL_SIGN_UP_SUBJECT, mailForm);
        return ResponseEntity.status(CREATED).build();
    }

    @PostMapping("/sign-in")
    @Operation(summary = "사용자 로그인", description = "사용자 로그인 진행")
    public ResponseEntity<UserApiResponse> signIn(
            @Valid @RequestBody UserSignInRequest request
    ) {
        CompletableFuture<Pair<String, String>> country =
                locationService.getCountry(
                        request.latitude(),
                        request.longitude()
                );
        final UserSignInResult userSignInResult = userSignInService.signIn(request.toCommand(), country);
        return ResponseEntity.status(OK).body(UserApiResponse.of(userSignInResult));
    }

    @PatchMapping("/information")
    @ExtractUserId
    @Operation(summary = "사용자 정보 수정", description = "사용자 정보 수정 진행")
    public ResponseEntity<UserApiResponse> updateInformation(
            HttpServletRequest request,
            @RequestBody UserInfoUpdateRequest userInfoUpdateRequest
    ) {
        Long userSeq = (Long) request.getAttribute("userSeq");
        CompletableFuture<Pair<String, String>> country =
                locationService.getCountry(
                        userInfoUpdateRequest.latitude(),
                        userInfoUpdateRequest.longitude()
                );
        userInformationService.updateInformation(
                userSeq, country, userInfoUpdateRequest.toCommand()
        );
        return ResponseEntity.status(OK).body(UserApiResponse.of(OK.value()));
    }

    @PatchMapping("/password/change")
    @ExtractUserId
    @Operation(summary = "비밀번호 변경", description = "비밀번호 변경 진행")
    public ResponseEntity<UserApiResponse> changePassword(
            HttpServletRequest request,
            @RequestBody UserPasswordChangeRequest userPasswordChangeRequest
    ) {
        Long userSeq = (Long) request.getAttribute("userSeq");
        userInformationService.changePassword(
                userSeq,
                userPasswordChangeRequest.toCommand()
        );

        return ResponseEntity.status(NO_CONTENT).build();
    }

    @GetMapping("/nicknames")
    @Operation(summary = "닉네임 중복 체크", description = "닉네임 중복 체크 진행")
    public ResponseEntity<UserApiResponse> checkNickname(
            @RequestParam String nickname
    ) {
        Boolean isNickNameAvailable = userSignUpService.checkNickname(nickname);
        return ResponseEntity.status(OK).body(UserApiResponse.of(isNickNameAvailable));
    }

    @PatchMapping("/{userSeq}/{status}")
    @Operation(summary = "사용자 차단/해제", description = "사용자 차단/해제 진행")
    public ResponseEntity<Void> blockUser(
            HttpServletRequest request,
            @PathVariable("userSeq") Long userSeq,
            @PathVariable("status") String status
    ) {
        Long adminSeq = (Long) request.getAttribute("userSeq");
        UserStatus userStatus = UserStatus.valueOf(status);
        userInformationService.blockOrUnblockUser(userSeq, adminSeq, userStatus);
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @PatchMapping("/{userSeq}/report")
    @ExtractUserId
    @Operation(summary = "사용자 신고", description = "사용자 신고 진행")
    public ResponseEntity<Void> reportUser(
            HttpServletRequest request,
            @PathVariable("userSeq") Long userSeq,
            @Valid @RequestBody UserReportRequest userReportRequest
    ) {
        Long reporterSeq = (Long) request.getAttribute("userSeq");
        userReportService.reportUser(
                userSeq,
                reporterSeq,
                userReportRequest.toCommand()
        );
        return ResponseEntity.status(NO_CONTENT).build();
    }

}
