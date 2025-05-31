//package com.backend.immilog.user.application;
//
//import com.backend.immilog.global.enums.Country;
//import com.backend.immilog.image.application.ImageUploader;
//import com.backend.immilog.user.application.command.UserPasswordChangeCommand;
//import com.backend.immilog.user.application.usecase.impl.UserUpdateService;
//import com.backend.immilog.user.application.services.UserCommandService;
//import com.backend.immilog.user.application.services.UserQueryService;
//import com.backend.immilog.user.domain.model.user.UserStatus;
//import com.backend.immilog.user.domain.model.user.Auth;
//import com.backend.immilog.user.domain.model.user.Location;
//import com.backend.immilog.user.domain.model.user.Profile;
//import com.backend.immilog.user.domain.model.user.User;
//import com.backend.immilog.user.exception.UserException;
//import com.backend.immilog.user.presentation.payload.UserInformationPayload;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.data.util.Pair;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.concurrent.CompletableFuture;
//
//import static com.backend.immilog.global.enums.UserRole.ROLE_ADMIN;
//import static com.backend.immilog.global.enums.UserRole.ROLE_USER;
//import static com.backend.immilog.user.exception.UserErrorCode.*;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.Mockito.*;
//
//@DisplayName("사용자 정보 서비스 테스트")
//class UserUpdateServiceTest {
//    private final UserQueryService userQueryService = mock(UserQueryService.class);
//    private final UserCommandService userCommandService = mock(UserCommandService.class);
//    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
//    private final ImageUploader imageUploader = mock(ImageUploader.class);
//    private final UserUpdateService userUpdateService = new UserUpdateService(
//            userQueryService,
//            userCommandService,
//            passwordEncoder,
//            imageUploader
//    );
//
//    @Test
//    @DisplayName("사용자 정보 업데이트")
//    void updateInformation() {
//        // given
//        Long userSeq = 1L;
//        //
////        public record User(
////                Long seq,
////                Auth auth,
////                UserRole userRole,
////                ReportData reportData,
////                Profile profile,
////                Location location,
////                UserStatus userStatus,
////                LocalDateTime updatedAt
////        ) {
//        User user = new User(
//                userSeq,
//                Auth.of("test@email.com", "password"),
//                ROLE_USER,
//                null,
//                Profile.of("test", "image", Country.SOUTH_KOREA),
//                Location.of(Country.MALAYSIA, "KL"),
//                UserStatus.PENDING,
//                null
//        );
//
//        UserInformationPayload.UserInfoUpdateRequest param = new UserInformationPayload.UserInfoUpdateRequest(
//                "newNickName",
//                "newImage",
//                Country.JAPAN,
//                Country.INDONESIA,
//                37.123456,
//                126.123456,
//                UserStatus.ACTIVE
//        );
//        when(userQueryService.getUserById(userSeq)).thenReturn(user);
//        CompletableFuture<Pair<String, String>> country =
//                CompletableFuture.completedFuture(Pair.of("Japan", "Tokyo"));
//        // when
//        userUpdateService.updateInformation(
//                userSeq,
//                country,
//                param.toCommand()
//        );
//        //then
//        verify(userCommandService).save(any());
//    }
//
//    @Test
//    @DisplayName("사용자 비밀번호 변경 - 성공")
//    void changePassword_success() {
//        // given
//        Long userSeq = 1L;
//        User user = new User(
//                userSeq,
//                Auth.of("test@email.com", "password"),
//                ROLE_USER,
//                null,
//                Profile.of("test", "image", Country.SOUTH_KOREA),
//                Location.of(Country.MALAYSIA, "KL"),
//                UserStatus.PENDING,
//                null
//        );
//        UserPasswordChangeCommand param = new UserPasswordChangeCommand("existingPassword", "newPassword");
//
//        when(userQueryService.getUserById(userSeq)).thenReturn(user);
//        when(passwordEncoder.matches("existingPassword", user.password())).thenReturn(true);
//        when(passwordEncoder.encode("newPassword")).thenReturn("encodedPassword");
//
//        // when
//        userUpdateService.changePassword(userSeq, param);
//
//        // then
//        verify(userCommandService).save(any());
//    }
//
//    @Test
//    @DisplayName("사용자 비밀번호 변경 - 실패")
//    void changePassword_fail() {
//        // given
//        Long userSeq = 1L;
//        User user = new User(
//                userSeq,
//                Auth.of("test@email.com", "password"),
//                ROLE_USER,
//                null,
//                Profile.of("test", "image", Country.SOUTH_KOREA),
//                Location.of(Country.MALAYSIA, "KL"),
//                UserStatus.PENDING,
//                null
//        );
//        UserPasswordChangeCommand param = new UserPasswordChangeCommand("existingPassword", "newPassword");
//
//        when(userQueryService.getUserById(userSeq)).thenReturn(user);
//        when(passwordEncoder.matches("existingPassword", user.password())).thenReturn(false);
//
//        // when & then
//        assertThatThrownBy(() -> userUpdateService.changePassword(userSeq, param))
//                .isInstanceOf(UserException.class)
//                .hasMessage(PASSWORD_NOT_MATCH.getMessage());
//    }
//
//    @Test
//    @DisplayName("사용자 차단/해제 - 성공")
//    void blockOrUnblockUser() {
//        // given
//        Long userSeq = 1L;
//        Long adminSeq = 2L;
//        String userStatus = "BLOCKED";
//        User user = new User(
//                userSeq,
//                Auth.of("test@email.com", "password"),
//                ROLE_USER,
//                null,
//                Profile.of("test", "image", Country.SOUTH_KOREA),
//                Location.of(Country.MALAYSIA, "KL"),
//                UserStatus.PENDING,
//                null
//        );
//        User admin = new User(
//                adminSeq,
//                Auth.of("test@email.com", "password")
//                , ROLE_ADMIN,
//                null,
//                Profile.of("test", "image", Country.SOUTH_KOREA),
//                Location.of(Country.MALAYSIA, "KL"),
//                UserStatus.PENDING,
//                null
//        );
//        when(userQueryService.getUserById(userSeq)).thenReturn(user);
//        when(userQueryService.getUserById(adminSeq)).thenReturn(admin);
//
//        // when
//        userUpdateService.blockOrUnblockUser(userSeq, adminSeq, userStatus);
//
//        // then
//        verify(userCommandService).save(any());
//    }
//
//    @Test
//    @DisplayName("사용자 차단/해제 - 실패:관리자 아님")
//    void blockOrUnblockUser_fail() {
//        // given
//        Long userSeq = 1L;
//        Long adminSeq = 2L;
//        String userStatus = "BLOCKED";
//        User admin = new User(
//                adminSeq,
//                Auth.of("test@email.com", "password")
//                , ROLE_USER,
//                null,
//                Profile.of("test", "image", Country.SOUTH_KOREA),
//                Location.of(Country.MALAYSIA, "KL"),
//                UserStatus.PENDING,
//                null
//        );
//        when(userQueryService.getUserById(adminSeq)).thenReturn(admin);
//
//        // when & then
//        assertThatThrownBy(() -> userUpdateService.blockOrUnblockUser(
//                userSeq,
//                adminSeq,
//                userStatus
//        ))
//                .isInstanceOf(UserException.class)
//                .hasMessage(NOT_AN_ADMIN_USER.getMessage());
//    }
//
//    @Test
//    @DisplayName("사용자 정보 업데이트 - 예외 발생")
//    void updateInformation_exception() {
//        // given
//        Long userSeq = 1L;
//        User user = new User(
//                userSeq,
//                Auth.of("test@email.com", "password"),
//                ROLE_USER,
//                null,
//                Profile.of("test", "image", Country.SOUTH_KOREA),
//                Location.of(Country.MALAYSIA, "KL"),
//                UserStatus.PENDING,
//                null
//        );
//        UserInformationPayload.UserInfoUpdateRequest param = new UserInformationPayload.UserInfoUpdateRequest(
//                "newNickName",
//                "newImage",
//                Country.JAPAN,
//                Country.INDONESIA,
//                37.123456,
//                126.123456,
//                UserStatus.ACTIVE
//        );
//
//        when(userQueryService.getUserById(userSeq)).thenReturn(user);
//        CompletableFuture<Pair<String, String>> country =
//                CompletableFuture.failedFuture(new InterruptedException("Country fetching failed"));
//
//        // when & then
//        userUpdateService.updateInformation(
//                userSeq,
//                country,
//                param.toCommand()
//        );
//    }
//
//    @Test
//    @DisplayName("비밀번호 변경 - 현재 비밀번호가 일치하지 않을 때")
//    void changePassword_passwordNotMatch() {
//        // given
//        Long userSeq = 1L;
//        User user = new User(
//                userSeq,
//                Auth.of("test@email.com", "password"),
//                ROLE_USER,
//                null,
//                Profile.of("test", "image", Country.SOUTH_KOREA),
//                Location.of(Country.MALAYSIA, "KL"),
//                UserStatus.PENDING,
//                null
//        );
//        UserPasswordChangeCommand param = new UserPasswordChangeCommand("wrongPassword", "newPassword");
//
//        when(userQueryService.getUserById(userSeq)).thenReturn(user);
//        when(passwordEncoder.matches("wrongPassword", user.password())).thenReturn(false);
//
//        // when & then
//        assertThatThrownBy(() -> userUpdateService.changePassword(userSeq, param))
//                .isInstanceOf(UserException.class)
//                .hasMessage(PASSWORD_NOT_MATCH.getMessage());
//    }
//
//    @Test
//    @DisplayName("프로필 이미지 변경 - 예전 이미지 삭제 및 새 이미지 업데이트")
//    void updateProfileImage() {
//        // given
//        Long userSeq = 1L;
//        User user = new User(
//                userSeq,
//                Auth.of("test@email.com", "password")
//                , ROLE_USER,
//                null,
//                Profile.of("test", "oldImage", Country.SOUTH_KOREA),
//                Location.of(Country.MALAYSIA, "KL"),
//                UserStatus.PENDING,
//                null
//        );
//        UserInformationPayload.UserInfoUpdateRequest param = new UserInformationPayload.UserInfoUpdateRequest(
//                "test",
//                "newImage",
//                Country.SOUTH_KOREA,
//                Country.MALAYSIA,
//                37.123456,
//                126.123456,
//                UserStatus.ACTIVE
//        );
//        when(userQueryService.getUserById(userSeq)).thenReturn(user);
//
//        // when
//        userUpdateService.updateInformation(
//                userSeq,
//                CompletableFuture.completedFuture(Pair.of("South Korea", "Seoul")),
//                param.toCommand()
//        );
//
//        // then
//        verify(imageUploader).deleteFile("oldImage");
//        verify(userCommandService).save(any());
//    }
//
//    @Test
//    @DisplayName("사용자 차단/해제 - 사용자가 존재하지 않을 때")
//    void blockOrUnblockUser_userNotFound() {
//        // given
//        Long userSeq = 1L;
//        Long adminSeq = 2L;
//        String userStatus = "BLOCKED";
//
//        when(userQueryService.getUserById(userSeq)).thenThrow(new UserException(USER_NOT_FOUND));
//        // when & then
//        assertThatThrownBy(() -> userUpdateService.blockOrUnblockUser(userSeq, adminSeq, userStatus))
//                .isInstanceOf(UserException.class)
//                .hasMessage(USER_NOT_FOUND.getMessage());
//    }
//
//    @Test
//    @DisplayName("사용자 정보 업데이트 - 닉네임이 변경되지 않을 때")
//    void updateInformation_nicknameNotChanged() {
//        // given
//        Long userSeq = 1L;
//        User user = new User(
//                userSeq,
//                Auth.of("test@emial.com", "password"),
//                ROLE_USER,
//                null,
//                Profile.of("testNickName", "image", Country.SOUTH_KOREA),
//                Location.of(Country.MALAYSIA, "KL"),
//                UserStatus.PENDING,
//                null
//        );
//        UserInformationPayload.UserInfoUpdateRequest param = new UserInformationPayload.UserInfoUpdateRequest(
//                "testNickName",
//                "newImage",
//                Country.SOUTH_KOREA,
//                Country.MALAYSIA,
//                37.123456,
//                126.123456,
//                UserStatus.ACTIVE
//        );
//        when(userQueryService.getUserById(userSeq)).thenReturn(user);
//
//        // when
//        userUpdateService.updateInformation(
//                userSeq,
//                CompletableFuture.completedFuture(Pair.of("South Korea", "Seoul")),
//                param.toCommand()
//        );
//
//        // then
//        verify(userCommandService, times(1)).save(any());
//    }
//}