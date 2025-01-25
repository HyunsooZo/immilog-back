package com.backend.immilog.user.application;

import com.backend.immilog.image.application.service.ImageService;
import com.backend.immilog.user.application.command.UserPasswordChangeCommand;
import com.backend.immilog.user.application.services.UserInformationService;
import com.backend.immilog.user.application.services.command.UserCommandService;
import com.backend.immilog.user.application.services.query.UserQueryService;
import com.backend.immilog.user.domain.enums.UserCountry;
import com.backend.immilog.user.domain.enums.UserStatus;
import com.backend.immilog.user.domain.model.user.Auth;
import com.backend.immilog.user.domain.model.user.Location;
import com.backend.immilog.user.domain.model.user.Profile;
import com.backend.immilog.user.domain.model.user.User;
import com.backend.immilog.user.exception.UserException;
import com.backend.immilog.user.presentation.request.UserInfoUpdateRequest;
import com.backend.immilog.user.presentation.request.UserPasswordChangeRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.backend.immilog.global.enums.UserRole.ROLE_ADMIN;
import static com.backend.immilog.global.enums.UserRole.ROLE_USER;
import static com.backend.immilog.user.exception.UserErrorCode.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("사용자 정보 서비스 테스트")
class UserInformationServiceTest {
    private final UserQueryService userQueryService = mock(UserQueryService.class);
    private final UserCommandService userCommandService = mock(UserCommandService.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final ImageService imageService = mock(ImageService.class);
    private final UserInformationService userInformationService = new UserInformationService(
            userQueryService,
            userCommandService,
            passwordEncoder,
            imageService
    );

    @Test
    @DisplayName("사용자 정보 업데이트")
    void updateInformation() {
        // given
        Long userSeq = 1L;
        User user = User.builder()
                .seq(userSeq)
                .auth(Auth.of("test@email.com", "password"))
                .profile(Profile.of("test", "image", UserCountry.SOUTH_KOREA))
                .userStatus(UserStatus.PENDING)
                .userRole(ROLE_USER)
                .location(Location.of(UserCountry.MALAYSIA, "KL"))
                .reportData(null)
                .build();

        UserInfoUpdateRequest param =
                UserInfoUpdateRequest.builder()
                        .nickName("newNickName")
                        .profileImage("newImage")
                        .country(UserCountry.JAPAN)
                        .interestCountry(UserCountry.INDONESIA)
                        .latitude(37.123456)
                        .longitude(126.123456)
                        .status(UserStatus.ACTIVE)
                        .build();

        when(userQueryService.getUserById(userSeq)).thenReturn(Optional.of(user));
        CompletableFuture<Pair<String, String>> country =
                CompletableFuture.completedFuture(Pair.of("Japan", "Tokyo"));
        // when
        userInformationService.updateInformation(
                userSeq,
                country,
                param.toCommand()
        );
        //then
        verify(userCommandService).save(any());
    }

    @Test
    @DisplayName("사용자 비밀번호 변경 - 성공")
    void changePassword_success() {
        // given
        Long userSeq = 1L;
        User user = User.builder()
                .seq(userSeq)
                .auth(Auth.of("test@email.com", "password"))
                .profile(Profile.of("test", "image", UserCountry.SOUTH_KOREA))
                .userStatus(UserStatus.PENDING)
                .userRole(ROLE_USER)
                .location(Location.of(UserCountry.MALAYSIA, "KL"))
                .reportData(null)
                .build();
        UserPasswordChangeCommand param = new UserPasswordChangeCommand("existingPassword", "newPassword");

        when(userQueryService.getUserById(userSeq)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("existingPassword", user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedPassword");

        // when
        userInformationService.changePassword(userSeq, param);

        // then
        verify(userCommandService).save(any());
    }

    @Test
    @DisplayName("사용자 비밀번호 변경 - 실패")
    void changePassword_fail() {
        // given
        Long userSeq = 1L;
        User user = User.builder()
                .seq(userSeq)
                .auth(Auth.of("test@email.com", "password"))
                .profile(Profile.of("test", "image", UserCountry.SOUTH_KOREA))
                .userStatus(UserStatus.PENDING)
                .userRole(ROLE_USER)
                .location(Location.of(UserCountry.MALAYSIA, "KL"))
                .reportData(null)
                .build();
        UserPasswordChangeRequest param = UserPasswordChangeRequest.builder()
                .existingPassword("existingPassword")
                .newPassword("newPassword")
                .build();

        when(userQueryService.getUserById(userSeq)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("existingPassword", user.getPassword())).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> userInformationService.changePassword(userSeq, param.toCommand()))
                .isInstanceOf(UserException.class)
                .hasMessage(PASSWORD_NOT_MATCH.getMessage());
    }

    @Test
    @DisplayName("사용자 차단/해제 - 성공")
    void blockOrUnblockUser() {
        // given
        Long userSeq = 1L;
        Long adminSeq = 2L;
        UserStatus userStatus = UserStatus.BLOCKED;
        User user = User.builder()
                .seq(userSeq)
                .auth(Auth.of("test@email.com", "password"))
                .profile(Profile.of("test", "image", UserCountry.SOUTH_KOREA))
                .userStatus(UserStatus.PENDING)
                .userRole(ROLE_USER)
                .location(Location.of(UserCountry.MALAYSIA, "KL"))
                .reportData(null)
                .userStatus(UserStatus.ACTIVE)
                .build();
        User admin = User.builder()
                .seq(userSeq)
                .auth(Auth.of("test@email.com", "password"))
                .profile(Profile.of("test", "image", UserCountry.SOUTH_KOREA))
                .userStatus(UserStatus.PENDING)
                .userRole(ROLE_ADMIN)
                .location(Location.of(UserCountry.MALAYSIA, "KL"))
                .reportData(null)
                .build();
        when(userQueryService.getUserById(userSeq)).thenReturn(Optional.of(user));
        when(userQueryService.getUserById(adminSeq)).thenReturn(Optional.of(admin));

        // when
        userInformationService.blockOrUnblockUser(userSeq, adminSeq, userStatus);

        // then
        verify(userCommandService).save(any());
    }

    @Test
    @DisplayName("사용자 차단/해제 - 실패:관리자 아님")
    void blockOrUnblockUser_fail() {
        // given
        Long userSeq = 1L;
        Long adminSeq = 2L;
        UserStatus userStatus = UserStatus.BLOCKED;
        User admin = User.builder()
                .seq(userSeq)
                .auth(Auth.of("test@email.com", "password"))
                .profile(Profile.of("test", "image", UserCountry.SOUTH_KOREA))
                .userStatus(UserStatus.PENDING)
                .userRole(ROLE_USER)
                .location(Location.of(UserCountry.MALAYSIA, "KL"))
                .reportData(null)
                .build();
        when(userQueryService.getUserById(adminSeq)).thenReturn(Optional.of(admin));

        // when & then
        assertThatThrownBy(() -> userInformationService.blockOrUnblockUser(
                userSeq,
                adminSeq,
                userStatus
        ))
                .isInstanceOf(UserException.class)
                .hasMessage(NOT_AN_ADMIN_USER.getMessage());
    }

    @Test
    @DisplayName("사용자 정보 업데이트 - 예외 발생")
    void updateInformation_exception() {
        // given
        Long userSeq = 1L;
        User user = User.builder()
                .seq(userSeq)
                .auth(Auth.of("test@email.com", "password"))
                .profile(Profile.of("test", "image", UserCountry.SOUTH_KOREA))
                .userStatus(UserStatus.PENDING)
                .userRole(ROLE_USER)
                .location(Location.of(UserCountry.MALAYSIA, "KL"))
                .reportData(null)
                .build();

        UserInfoUpdateRequest param =
                UserInfoUpdateRequest.builder()
                        .profileImage("newImage")
                        .country(UserCountry.JAPAN)
                        .interestCountry(UserCountry.INDONESIA)
                        .latitude(37.123456)
                        .longitude(126.123456)
                        .status(UserStatus.ACTIVE)
                        .build();

        when(userQueryService.getUserById(userSeq)).thenReturn(Optional.of(user));
        CompletableFuture<Pair<String, String>> country =
                CompletableFuture.failedFuture(new InterruptedException("Country fetching failed"));

        // when & then
        userInformationService.updateInformation(
                userSeq,
                country,
                param.toCommand()
        );
    }

    @Test
    @DisplayName("비밀번호 변경 - 현재 비밀번호가 일치하지 않을 때")
    void changePassword_passwordNotMatch() {
        // given
        Long userSeq = 1L;
        User user = User.builder()
                .seq(userSeq)
                .auth(Auth.of("test@email.com", "password"))
                .profile(Profile.of("test", "image", UserCountry.SOUTH_KOREA))
                .userStatus(UserStatus.PENDING)
                .userRole(ROLE_USER)
                .location(Location.of(UserCountry.MALAYSIA, "KL"))
                .reportData(null)
                .build();
        UserPasswordChangeCommand param = new UserPasswordChangeCommand("wrongPassword", "newPassword");

        when(userQueryService.getUserById(userSeq)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", user.getPassword())).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> userInformationService.changePassword(userSeq, param))
                .isInstanceOf(UserException.class)
                .hasMessage(PASSWORD_NOT_MATCH.getMessage());
    }

    @Test
    @DisplayName("프로필 이미지 변경 - 예전 이미지 삭제 및 새 이미지 업데이트")
    void updateProfileImage() {
        // given
        Long userSeq = 1L;
        User user = User.builder()
                .seq(userSeq)
                .auth(Auth.of("test@email.com", "password"))
                .profile(Profile.of("test", "oldImage", UserCountry.SOUTH_KOREA))
                .userStatus(UserStatus.PENDING)
                .userRole(ROLE_USER)
                .location(Location.of(UserCountry.MALAYSIA, "KL"))
                .reportData(null)
                .build();
        UserInfoUpdateRequest param =
                UserInfoUpdateRequest.builder()
                        .nickName("newNickName")
                        .profileImage("newImage")
                        .build();

        when(userQueryService.getUserById(userSeq)).thenReturn(Optional.of(user));

        // when
        userInformationService.updateInformation(
                userSeq,
                CompletableFuture.completedFuture(Pair.of("South Korea", "Seoul")),
                param.toCommand()
        );

        // then
        verify(imageService).deleteFile("oldImage");
        verify(userCommandService).save(any());
    }

    @Test
    @DisplayName("사용자 차단/해제 - 사용자가 존재하지 않을 때")
    void blockOrUnblockUser_userNotFound() {
        // given
        Long userSeq = 1L;
        Long adminSeq = 2L;
        UserStatus userStatus = UserStatus.BLOCKED;

        when(userQueryService.getUserById(userSeq)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userInformationService.blockOrUnblockUser(userSeq, adminSeq, userStatus))
                .isInstanceOf(UserException.class)
                .hasMessage(USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("사용자 정보 업데이트 - 닉네임이 변경되지 않을 때")
    void updateInformation_nicknameNotChanged() {
        // given
        Long userSeq = 1L;
        User user = User.builder()
                .seq(userSeq)
                .auth(Auth.of("test@email.com", "password"))
                .profile(Profile.of("testNickName", "image", UserCountry.SOUTH_KOREA))
                .userStatus(UserStatus.PENDING)
                .userRole(ROLE_USER)
                .location(Location.of(UserCountry.MALAYSIA, "KL"))
                .reportData(null)
                .build();

        UserInfoUpdateRequest param =
                UserInfoUpdateRequest.builder()
                        .nickName("testNickName") // 동일한 닉네임 입력
                        .build();

        when(userQueryService.getUserById(userSeq)).thenReturn(Optional.of(user));

        // when
        userInformationService.updateInformation(
                userSeq,
                CompletableFuture.completedFuture(Pair.of("South Korea", "Seoul")),
                param.toCommand()
        );

        // then
        verify(userCommandService, times(1)).save(any());
    }
}