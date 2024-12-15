package com.backend.immilog.user.application;

import com.backend.immilog.global.enums.GlobalCountry;
import com.backend.immilog.global.enums.UserRole;
import com.backend.immilog.global.security.TokenProvider;
import com.backend.immilog.user.application.result.UserSignInResult;
import com.backend.immilog.user.application.services.UserSignInService;
import com.backend.immilog.user.application.services.command.RefreshTokenCommandService;
import com.backend.immilog.user.application.services.query.UserQueryService;
import com.backend.immilog.user.application.services.query.RefreshTokenQueryService;
import com.backend.immilog.user.domain.enums.UserStatus;
import com.backend.immilog.user.domain.model.user.Location;
import com.backend.immilog.user.domain.model.user.User;
import com.backend.immilog.user.exception.UserException;
import com.backend.immilog.user.presentation.request.UserSignInRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.backend.immilog.global.enums.UserRole.ROLE_USER;
import static com.backend.immilog.user.domain.enums.UserCountry.MALAYSIA;
import static com.backend.immilog.user.domain.enums.UserCountry.SOUTH_KOREA;
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
    private final RefreshTokenQueryService refreshTokenQueryService = mock(RefreshTokenQueryService.class);
    private final RefreshTokenCommandService refreshTokenCommandService = mock(RefreshTokenCommandService.class);
    private final CompletableFuture<Pair<String, String>> country = mock(CompletableFuture.class);

    private final UserSignInService userSignInService = new UserSignInService(
            userQueryService,
            passwordEncoder,
            tokenProvider,
            refreshTokenQueryService,
            refreshTokenCommandService
    );

    @Test
    @DisplayName("로그인 성공")
    void sign_in_success() {
        // given
        UserSignInRequest userSignInRequest = UserSignInRequest.builder()
                .email("test@email.com")
                .password("test1234")
                .latitude(37.1234)
                .longitude(127.1234)
                .build();

        Location location = Location.builder()
                .country(SOUTH_KOREA)
                .region("서울")
                .build();

        User user = User.builder()
                .seq(1L)
                .email(userSignInRequest.email())
                .userStatus(UserStatus.ACTIVE)
                .userRole(UserRole.ROLE_USER)
                .password(passwordEncoder.encode(userSignInRequest.password()))
                .location(location)
                .build();

        when(userQueryService.getUserByEmail(userSignInRequest.email()))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches(userSignInRequest.password(), user.getPassword()))
                .thenReturn(true);
        when(tokenProvider.issueAccessToken(
                anyLong(),
                anyString(),
                any(UserRole.class),
                any(GlobalCountry.class)
        )).thenReturn("accessToken");
        when(tokenProvider.issueRefreshToken())
                .thenReturn("refreshToken");
        when(country.orTimeout(5, SECONDS))
                .thenReturn(CompletableFuture.completedFuture(Pair.of("대한민국", "서울")));

        // when
        UserSignInResult userSignInResult =
                userSignInService.signIn(userSignInRequest.toCommand(), country);

        // then
        assertThat(userSignInResult.userSeq()).isEqualTo(user.getSeq());
        assertThat(userSignInResult.accessToken()).isEqualTo("accessToken");
        verify(tokenProvider, times(1)).issueAccessToken(
                anyLong(),
                anyString(),
                any(UserRole.class),
                any(GlobalCountry.class)
        );
        verify(tokenProvider, times(1)).issueRefreshToken();
    }

    @Test
    @DisplayName("로그인 실패: 사용자 없음")
    void sign_in_fail_user_not_found() {
        // given
        UserSignInRequest userSignInRequest = UserSignInRequest.builder()
                .email("test@email.com")
                .password("test1234")
                .latitude(37.1234)
                .longitude(127.1234)
                .build();

        Location location = Location.builder()
                .country(SOUTH_KOREA)
                .region("서울")
                .build();

        User user = User.builder()
                .seq(1L)
                .email(userSignInRequest.email())
                .userStatus(UserStatus.ACTIVE)
                .userRole(UserRole.ROLE_USER)
                .password(passwordEncoder.encode(userSignInRequest.password()))
                .location(location)
                .build();

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
        User user = User.builder()
                .seq(userSeq)
                .email("test@email.com")
                .nickName("test")
                .imageUrl("image")
                .userStatus(UserStatus.PENDING)
                .userRole(ROLE_USER)
                .interestCountry(SOUTH_KOREA)
                .location(Location.of(SOUTH_KOREA, "Seoul"))
                .reportInfo(null)
                .build();

        Pair<String, String> country = Pair.of("South Korea", "Seoul");
        when(userQueryService.getUserById(userSeq)).thenReturn(Optional.of(user));
        when(tokenProvider.issueAccessToken(
                anyLong(),
                anyString(),
                any(UserRole.class),
                any(GlobalCountry.class)
        )).thenReturn("accessToken");
        when(tokenProvider.issueRefreshToken()).thenReturn("refreshToken");

        // when
        UserSignInResult result = userSignInService.getUserSignInDTO(userSeq, country);
        // then
        verify(refreshTokenCommandService, times(1)).saveKeyAndValue(
                "Refresh: refreshToken", user.getEmail(), 5 * 29 * 24 * 60
        );
        assertThat(result.userSeq()).isEqualTo(userSeq);
    }

    @Test
    @DisplayName("사용자 정보 조회 성공 : 위치 정보 불 일치")
    void getUserSignInDTO_success_location_not_match() {
        // given
        Long userSeq = 1L;
        User user = User.builder()
                .seq(userSeq)
                .email("test@email.com")
                .nickName("test")
                .imageUrl("image")
                .userStatus(UserStatus.PENDING)
                .userRole(ROLE_USER)
                .interestCountry(SOUTH_KOREA)
                .location(Location.of(MALAYSIA, "KL"))
                .reportInfo(null)
                .build();

        Pair<String, String> country = Pair.of("South Korea", "Seoul");
        when(userQueryService.getUserById(userSeq)).thenReturn(Optional.of(user));
        when(tokenProvider.issueAccessToken(
                anyLong(),
                anyString(),
                any(UserRole.class),
                any(GlobalCountry.class)
        )).thenReturn("accessToken");
        when(tokenProvider.issueRefreshToken()).thenReturn("refreshToken");

        // when
        UserSignInResult result = userSignInService.getUserSignInDTO(userSeq, country);
        // then
        verify(refreshTokenCommandService, times(1)).saveKeyAndValue(
                "Refresh: refreshToken", user.getEmail(), 5 * 29 * 24 * 60
        );
        assertThat(result.userSeq()).isEqualTo(userSeq);
    }
}