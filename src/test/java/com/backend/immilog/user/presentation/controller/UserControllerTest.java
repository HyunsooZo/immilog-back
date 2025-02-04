package com.backend.immilog.user.presentation.controller;


import com.backend.immilog.global.enums.Country;
import com.backend.immilog.user.application.result.UserSignInResult;
import com.backend.immilog.user.application.services.*;
import com.backend.immilog.user.domain.enums.ReportReason;
import com.backend.immilog.user.domain.enums.UserStatus;
import com.backend.immilog.user.enums.EmailComponents;
import com.backend.immilog.user.presentation.request.*;
import com.backend.immilog.user.presentation.response.UserApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static com.backend.immilog.user.domain.enums.ReportReason.FRAUD;
import static com.backend.immilog.user.domain.enums.UserStatus.ACTIVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

@DisplayName("사용자 컨트롤러 테스트")
class UserControllerTest {
    private final UserSignUpService userSignUpService = mock(UserSignUpService.class);
    private final UserSignInService userSignInService = mock(UserSignInService.class);
    private final LocationService locationService = mock(LocationService.class);
    private final UserInformationService userInformationService = mock(UserInformationService.class);
    private final UserReportService userReportService = mock(UserReportService.class);
    private final EmailService emailService = mock(EmailService.class);
    private final UserController userController = new UserController(
            userSignUpService,
            userSignInService,
            userInformationService,
            userReportService,
            locationService,
            emailService
    );

    @Test
    @DisplayName("회원가입")
    void signUp() {
        // given
        UserSignUpRequest param = new UserSignUpRequest(
                "test",
                "test1234",
                "email@email.com",
                "SOUTH_KOREA",
                "SOUTH_KOREA",
                "Seoul",
                "image"
        );
        when(userSignUpService.signUp(param.toCommand())).thenReturn(Pair.of(1L, "test"));

        // when
        ResponseEntity<UserApiResponse> response = userController.signUp(param);

        // then
        verify(emailService, times(1)).sendHtmlEmail(
                param.email(),
                EmailComponents.EMAIL_SIGN_UP_SUBJECT,
                String.format(EmailComponents.HTML_SIGN_UP_CONTENT, "test", String.format(EmailComponents.API_LINK, 1L))
        );
        assertThat(response.getStatusCode()).isEqualTo(CREATED);
    }

    @Test
    @DisplayName("로그인")
    void signIn() {
        // given
        UserSignInRequest param = new UserSignInRequest("email", "password", 37.1234, 127.1234);
        when(locationService.getCountry(param.latitude(), param.longitude())).thenReturn(CompletableFuture.completedFuture(Pair.of("대한민국", "서울")));
        when(userSignInService.signIn(param.toCommand(), locationService.getCountry(param.latitude(), param.longitude()))).thenReturn(mock(UserSignInResult.class));
        // when
        ResponseEntity<UserApiResponse> response = userController.signIn(param);

        // then
        assertThat(response.getStatusCode()).isEqualTo(OK);
    }

    @Test
    @DisplayName("사용자 정보 수정")
    void updateInformation() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        UserInfoUpdateRequest param =
                new UserInfoUpdateRequest(
                        "newNickName",
                        "newImage",
                        Country.JAPAN,
                        Country.INDONESIA,
                        37.123456,
                        126.123456,
                        UserStatus.ACTIVE
                );

        Long userSeq = 1L;
        when(locationService.getCountry(param.latitude(), param.longitude())).thenReturn(CompletableFuture.completedFuture(Pair.of("Japan", "Tokyo")));
        // when
        ResponseEntity<UserApiResponse> response = userController.updateInformation(userSeq, param);

        // then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        verify(userInformationService, times(1)).updateInformation(1L, locationService.getCountry(param.latitude(), param.longitude()), param.toCommand());
    }

    @Test
    @DisplayName("사용자 비밀번호 변경")
    void resetPassword() {
        // given
        Long userSeq = 1L;
        UserPasswordChangeRequest param = new UserPasswordChangeRequest(
                "existingPassword",
                "newPassword"
        );
        // when
        ResponseEntity<UserApiResponse> response = userController.changePassword(userSeq, param);

        // then
        verify(userInformationService, times(1))
                .changePassword(1L, param.toCommand());
        assertThat(response.getStatusCode()).isEqualTo(NO_CONTENT);
    }

    @Test
    @DisplayName("닉네임 중복여부 체크")
    void checkNickname() {
        // given
        String nickname = "test";
        when(userSignUpService.isNicknameAvailable(nickname)).thenReturn(true);

        // when
        ResponseEntity<UserApiResponse> response = userController.checkNickname(nickname);

        // then
        UserApiResponse body = Objects.requireNonNull(response.getBody());
        assertThat(body.data()).isEqualTo(true);
    }

    @Test
    @DisplayName("사용자 차단")
    void blockUser() {
        // given
        Long userSeq = 1L;
        Long adminSeq = 2L;
        String token = "token";
        String status = "BLOCKED";
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("userSeq")).thenReturn(1L);

        // when
        ResponseEntity<Void> response = userController.blockUser(userSeq, userSeq, status);

        // then
        assertThat(response.getStatusCode()).isEqualTo(NO_CONTENT);
    }

    @Test
    @DisplayName("사용자 신고")
    void reportUser() {
        // given
        Long targetUserSeq = 2L;
        Long userSeq = 1L;
        UserReportRequest param = new UserReportRequest(
                ReportReason.FRAUD,
                "test"
        );
        // when
        ResponseEntity<Void> response = userController.reportUser(userSeq, targetUserSeq, param);
        // then
        assertThat(response.getStatusCode()).isEqualTo(NO_CONTENT);
        verify(userReportService, times(1)).reportUser(targetUserSeq, userSeq, param.toCommand());
    }
}