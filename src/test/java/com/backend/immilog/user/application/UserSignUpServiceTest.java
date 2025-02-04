package com.backend.immilog.user.application;

import com.backend.immilog.user.application.services.UserSignUpService;
import com.backend.immilog.user.application.services.command.UserCommandService;
import com.backend.immilog.user.application.services.query.UserQueryService;
import com.backend.immilog.user.domain.enums.UserStatus;
import com.backend.immilog.user.domain.model.user.Profile;
import com.backend.immilog.user.domain.model.user.User;
import com.backend.immilog.user.exception.UserException;
import com.backend.immilog.user.presentation.request.UserSignInRequest;
import com.backend.immilog.user.presentation.request.UserSignUpRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.backend.immilog.user.exception.UserErrorCode.EXISTING_USER;
import static com.backend.immilog.user.exception.UserErrorCode.USER_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("사용자 회원가입 서비스 테스트")
class UserSignUpServiceTest {
    private final UserQueryService userQueryService = mock(UserQueryService.class);
    private final UserCommandService userCommandService = mock(UserCommandService.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final UserSignUpService userSignUpService = new UserSignUpService(
            userQueryService,
            userCommandService,
            passwordEncoder
    );

    @Test
    @DisplayName("회원가입 - 성공")
    void signUp_success() {
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
        User user = new User(
                1L,
                null,
                null,
                null,
                Profile.of("test",null,null),
                null,
                null,
                null
        );
        when(userCommandService.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("test1234");
        Pair<Long, String> seqAndNickName = userSignUpService.signUp(param.toCommand());
        // then
        assertThat(seqAndNickName.getSecond()).isEqualTo("test");
    }

    @Test
    @DisplayName("회원가입 - 실패(이미 존재하는 사용자)")
    void signUp_fail_() {
        UserSignUpRequest param = new  UserSignUpRequest(
                "test",
                "test1234",
                "email@email.com",
                "SOUTH_KOREA",
                "SOUTH_KOREA",
                "Seoul",
                "image"
        );
        when(userQueryService.isUserAlreadyExist(anyString())).thenReturn(true);
        assertThatThrownBy(() -> userSignUpService.signUp(param.toCommand()))
                .isInstanceOf(UserException.class)
                .hasMessage(EXISTING_USER.getMessage());
    }

    @Test
    @DisplayName("닉네임 중복 확인 - 중복되지 않은 경우")
    void isNickname_Taken_success() {
        // given
        String nickname = "test";
        when(userQueryService.isNicknameAvailable(nickname)).thenReturn(true);
        // when
        Boolean result = userSignUpService.isNicknameAvailable(nickname);
        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("닉네임 중복 확인 - 중복된 경우")
    void isNickname_Taken_fail() {
        // given
        String nickname = "test";
        when(userQueryService.isNicknameAvailable(nickname)).thenReturn(false);
        // when
        Boolean result = userSignUpService.isNicknameAvailable(nickname);
        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("이메일 인증 - 성공")
    void verifyEmail_success() {
        // given
        Long userSeq = 1L;
        User user = new User(
                1L,
                null,
                null,
                null,
                null,
                null,
                UserStatus.PENDING,
                null
        );
        when(userQueryService.getUserById(userSeq)).thenReturn(user);
        // when
        Pair<String, Boolean> result = userSignUpService.verifyEmail(userSeq);
        // then
        assertThat(result.getSecond()).isTrue();
    }

    @Test
    @DisplayName("이메일 인증 - 실패(사용자 없음)")
    void verifyEmail_fail_userNotFound() {
        // given
        Long userSeq = 1L;
        when(userQueryService.getUserById(userSeq)).thenThrow(new UserException(USER_NOT_FOUND));
        // when & then
        assertThatThrownBy(() -> userSignUpService.verifyEmail(userSeq))
                .isInstanceOf(UserException.class)
                .hasMessage(USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("이메일 인증 - 실패(이미 인증된 사용자)")
    void verifyEmail_fail_already_verified() {
        // given
        Long userSeq = 1L;
        User user = new User(
                1L,
                null,
                null,
                null,
                null,
                null,
                UserStatus.ACTIVE,
                null
        );
        when(userQueryService.getUserById(userSeq)).thenReturn(user);
        // when
        Pair<String, Boolean> result = userSignUpService.verifyEmail(userSeq);
        // then
        assertThat(result.getFirst()).isEqualTo("이미 인증된 사용자입니다.");
        assertThat(result.getSecond()).isTrue();
    }

    @Test
    @DisplayName("이메일 인증 - 실패(차단된 사용자)")
    void verifyEmail_fail_blocked() {
        // given
        Long userSeq = 1L;
        User user = new User(
                1L,
                null,
                null,
                null,
                null,
                null,
                UserStatus.BLOCKED,
                null
        );
        when(userQueryService.getUserById(userSeq)).thenReturn(user);
        // when
        Pair<String, Boolean> result = userSignUpService.verifyEmail(userSeq);
        // then
        assertThat(result.getFirst()).isEqualTo("차단된 사용자입니다.");
        assertThat(result.getSecond()).isFalse();
    }

    @Test
    @DisplayName("이메일 인증 - 실패(차단된 사용자)")
    void verifyEmail_fail_other_cases() {
        // given
        Long userSeq = 1L;

        User user = new User(
                1L,
                null,
                null,
                null,
                null,
                null,
                UserStatus.REPORTED,
                null
        );
        when(userQueryService.getUserById(userSeq)).thenReturn(user);
        // when
        Pair<String, Boolean> result = userSignUpService.verifyEmail(userSeq);
        // then
        assertThat(result.getFirst()).isEqualTo("이메일 인증이 필요한 사용자가 아닙니다.");
        assertThat(result.getSecond()).isTrue();
    }

}