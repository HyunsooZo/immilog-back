package com.backend.immilog.user.presentation.controller;

import com.backend.immilog.user.application.usecase.*;
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
    private final UserSignUpUseCase.UserSignUpProcessor userSignUpProcessor;
    private final UserSignInUseCase.UserLoginProcessor userLoginProcessor;
    private final UserUpdateUseCase.UserUpdater userUpdater;
    private final UserRepostUseCase.UserReporter userReporter;
    private final LocationFetchUseCase.LocationFetcher locationFetcher;
    private final EmailSendUseCase.EmailSender emailSender;

    public UserController(
            UserSignUpUseCase.UserSignUpProcessor userSignUpProcessor,
            UserSignInUseCase.UserLoginProcessor userLoginProcessor,
            UserUpdateUseCase.UserUpdater userUpdater,
            UserRepostUseCase.UserReporter userReporter,
            LocationFetchUseCase.LocationFetcher locationFetcher,
            EmailSendUseCase.EmailSender emailSender
    ) {
        this.userSignUpProcessor = userSignUpProcessor;
        this.userLoginProcessor = userLoginProcessor;
        this.userUpdater = userUpdater;
        this.userReporter = userReporter;
        this.locationFetcher = locationFetcher;
        this.emailSender = emailSender;
    }

    @PostMapping
    @Operation(summary = "사용자 회원가입", description = "사용자 회원가입 진행")
    public ResponseEntity<UserGeneralResponse> signUp(
            @Valid @RequestBody UserSignUpPayload.UserSignUpRequest request
    ) {
        final var userSeqAndName = userSignUpProcessor.signUp(request.toCommand());
        final var url = String.format(API_LINK, userSeqAndName.userSeq());
        final var mailForm = String.format(HTML_SIGN_UP_CONTENT, userSeqAndName.nickName(), url);
        emailSender.sendHtmlEmail(request.email(), EMAIL_SIGN_UP_SUBJECT, mailForm);

        return ResponseEntity.status(CREATED).build();
    }

    @PostMapping("/sign-in")
    @Operation(summary = "사용자 로그인", description = "사용자 로그인 진행")
    public ResponseEntity<UserSignInPayload.UserSignInResponse> signIn(
            @Valid @RequestBody UserSignInPayload.UserSignInRequest request
    ) {
        var country = locationFetcher.getCountry(request.latitude(), request.longitude());
        final var userSignInResult = userLoginProcessor.signIn(request.toCommand(), country);
        return ResponseEntity.status(OK).body(userSignInResult.toResponse());
    }

    @PatchMapping("/{userSeq}/information")
    @Operation(summary = "사용자 정보 수정", description = "사용자 정보 수정 진행")
    public ResponseEntity<UserGeneralResponse> updateInformation(
            @Parameter(description = "사용자 고유번호") @PathVariable("userSeq") Long userSeq,
            @RequestBody UserInformationPayload.UserInfoUpdateRequest request
    ) {
        var country = locationFetcher.getCountry(request.latitude(), request.longitude());
        userUpdater.updateInformation(userSeq, country, request.toCommand());
        return ResponseEntity.status(OK).body(UserGeneralResponse.success());
    }

    @PatchMapping("/{userSeq}/password/change")
    @Operation(summary = "비밀번호 변경", description = "비밀번호 변경 진행")
    public ResponseEntity<Void> changePassword(
            @Parameter(description = "사용자 고유번호") @PathVariable("userSeq") Long userSeq,
            @RequestBody UserInformationPayload.UserPasswordChangeRequest request
    ) {
        userUpdater.changePassword(userSeq, request.toCommand());
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @GetMapping("/nicknames")
    @Operation(summary = "닉네임 중복 체크", description = "닉네임 중복 체크 진행")
    public ResponseEntity<UserInformationPayload.UserNicknameResponse> checkNickname(
            @Parameter(description = "닉네임") @RequestParam("nickname") String nickname
    ) {
        var isNicknameAvailable = userSignUpProcessor.isNicknameAvailable(nickname);
        return ResponseEntity.status(OK).body(new UserInformationPayload.UserNicknameResponse(isNicknameAvailable));
    }

    @PatchMapping("/{userSeq}/targets/{targetSeq}/{status}")
    @Operation(summary = "사용자 차단/해제", description = "사용자 차단/해제 진행")
    public ResponseEntity<Void> blockUser(
            @Parameter(description = "사용자 고유번호") @PathVariable("userSeq") Long userSeq,
            @Parameter(description = "대상 사용자 고유번호") @PathVariable("targetSeq") Long targetSeq,
            @Parameter(description = "상태") @PathVariable("status") String status
    ) {
        userUpdater.blockOrUnblockUser(targetSeq, userSeq, status);
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @PatchMapping("/{userSeq}/targets/{targetSeq}/report")
    @Operation(summary = "사용자 신고", description = "사용자 신고 진행")
    public ResponseEntity<Void> reportUser(
            @Parameter(description = "사용자 고유번호") @PathVariable("userSeq") Long userSeq,
            @Parameter(description = "대상 사용자 고유번호") @PathVariable("targetSeq") Long targetSeq,
            @Valid @RequestBody UserInformationPayload.UserReportRequest request
    ) {
        userReporter.reportUser(targetSeq, userSeq, request.toCommand());
        return ResponseEntity.status(NO_CONTENT).build();
    }

}
