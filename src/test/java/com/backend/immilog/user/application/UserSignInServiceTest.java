package com.backend.immilog.user.application;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.global.enums.UserRole;
import com.backend.immilog.global.security.TokenProvider;
import com.backend.immilog.user.application.result.UserSignInResult;
import com.backend.immilog.user.application.services.UserSignInService;
import com.backend.immilog.user.application.services.command.RefreshTokenCommandService;
import com.backend.immilog.user.application.services.query.UserQueryService;
import com.backend.immilog.user.domain.enums.UserStatus;
import com.backend.immilog.user.domain.model.user.*;
import com.backend.immilog.user.exception.UserException;
import com.backend.immilog.user.presentation.request.UserSignInRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

import static com.backend.immilog.global.enums.UserRole.ROLE_USER;
import static com.backend.immilog.user.exception.UserErrorCode.USER_NOT_FOUND;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("사용자 로그인 서비스 테스트")
class UserSignInServiceTest {
    private final UserQueryService userQueryService = mock(UserQueryService.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final TokenProvider tokenProvider = mock(TokenProvider.class);
    private final RefreshTokenCommandService refreshTokenCommandService = mock(RefreshTokenCommandService.class);
    private final CompletableFuture<Pair<String, String>> country = mock(CompletableFuture.class);

    private final UserSignInService userSignInService = new UserSignInService(
            userQueryService,
            passwordEncoder,
            tokenProvider,
            refreshTokenCommandService
    );

    @Test
    @DisplayName("로그인 성공")
    void sign_in_success() {
        // given
        UserSignInRequest userSignInRequest = new UserSignInRequest(
                "test@email.com",
                "test1234",
                37.1234,
                127.1234
        );
        Location location = Location.of(Country.SOUTH_KOREA, "서울");
        User user = new User(
                1L,
                Auth.of(userSignInRequest.email(), passwordEncoder.encode(userSignInRequest.password())),
                ROLE_USER,
                null,
                Profile.of("test", "image", Country.SOUTH_KOREA),
                location,
                UserStatus.ACTIVE,
                LocalDateTime.now()
        );

        when(userQueryService.getUserByEmail(userSignInRequest.email())).thenReturn(user);
        when(passwordEncoder.matches(userSignInRequest.password(), user.password())).thenReturn(true);
        when(tokenProvider.issueAccessToken(anyLong(), anyString(), any(UserRole.class), any(Country.class))).thenReturn("accessToken");
        when(tokenProvider.issueRefreshToken()).thenReturn("refreshToken");
        when(country.orTimeout(5, SECONDS)).thenReturn(CompletableFuture.completedFuture(Pair.of("대한민국", "서울")));

        // when
        UserSignInResult userSignInResult =
                userSignInService.signIn(userSignInRequest.toCommand(), country);

        // then
        assertThat(userSignInResult.userSeq()).isEqualTo(user.seq());
        assertThat(userSignInResult.accessToken()).isEqualTo("accessToken");
        verify(tokenProvider, times(1)).issueAccessToken(
                anyLong(),
                anyString(),
                any(UserRole.class),
                any(Country.class)
        );
        verify(tokenProvider, times(1)).issueRefreshToken();
    }

    @Test
    @DisplayName("로그인 실패: 사용자 없음")
    void sign_in_fail_user_not_found() {
        // given
        UserSignInRequest userSignInRequest = new UserSignInRequest(
                "test@email.com",
                "test1234",
                37.1234,
                127.1234
        );

        Location location = Location.of(Country.SOUTH_KOREA, "서울");

        User user = new User(
                1L,
                Auth.of(userSignInRequest.email(), passwordEncoder.encode(userSignInRequest.password())),
                ROLE_USER,
                null,
                Profile.of("test", "image", Country.SOUTH_KOREA),
                location,
                UserStatus.ACTIVE,
                LocalDateTime.now()
        );
        when(userQueryService.getUserByEmail(userSignInRequest.email())).thenThrow(new UserException(USER_NOT_FOUND));
        // when & then
        assertThatThrownBy(() -> {
            userSignInService.signIn(userSignInRequest.toCommand(), country);
        })
                .isInstanceOf(UserException.class)
                .hasMessage(USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("사용자 정보 조회 성공 : 위치 정보 일치")
    void getUserSignInDTO_success() {
        // given
        Long userSeq = 1L;
        String mail = "test@email.com";
        User user = new User(
                1L,
                Auth.of(mail, "test"),
                ROLE_USER,
                null,
                Profile.of("test", "image", Country.SOUTH_KOREA),
                Location.of(Country.SOUTH_KOREA, "Seoul"),
                UserStatus.PENDING,
                LocalDateTime.now()
        );

        Pair<String, String> country = Pair.of("South Korea", "Seoul");
        when(userQueryService.getUserById(userSeq)).thenReturn(user);
        when(tokenProvider.issueAccessToken(
                anyLong(),
                anyString(),
                any(UserRole.class),
                any(Country.class)
        )).thenReturn("accessToken");
        when(tokenProvider.issueRefreshToken()).thenReturn("refreshToken");

        // when
        UserSignInResult result = userSignInService.getUserSignInDTO(userSeq, country);
        // then
        verify(refreshTokenCommandService, times(1)).saveKeyAndValue(
                "Refresh: refreshToken", user.email(), 5 * 29 * 24 * 60
        );
        assertThat(result.userSeq()).isEqualTo(userSeq);
    }

    @Test
    @DisplayName("사용자 정보 조회 성공 : 위치 정보 불 일치")
    void getUserSignInDTO_success_location_not_match() {
        // given
        Long userSeq = 1L;
        String mail = "test@email.com";
        User user = new User(
                1L,
                Auth.of(mail, "test"),
                ROLE_USER,
                null,
                Profile.of("test", "image", Country.SOUTH_KOREA),
                Location.of(Country.SOUTH_KOREA, "Seoul"),
                UserStatus.PENDING,
                LocalDateTime.now()
        );

        Pair<String, String> country = Pair.of("South Korea", "Seoul");
        when(userQueryService.getUserById(userSeq)).thenReturn(user);
        when(tokenProvider.issueAccessToken(
                anyLong(),
                anyString(),
                any(UserRole.class),
                any(Country.class)
        )).thenReturn("accessToken");
        when(tokenProvider.issueRefreshToken()).thenReturn("refreshToken");

        // when
        UserSignInResult result = userSignInService.getUserSignInDTO(userSeq, country);
        // then
        verify(refreshTokenCommandService, times(1)).saveKeyAndValue(
                "Refresh: refreshToken", user.email(), 5 * 29 * 24 * 60
        );
        assertThat(result.userSeq()).isEqualTo(userSeq);
    }
}