package com.backend.immilog.user.application.usecase.impl;

import com.backend.immilog.global.security.TokenProvider;
import com.backend.immilog.user.application.command.UserSignInCommand;
import com.backend.immilog.user.application.result.UserSignInResult;
import com.backend.immilog.user.application.services.RefreshTokenCommandService;
import com.backend.immilog.user.application.services.UserQueryService;
import com.backend.immilog.user.application.usecase.UserSignInUseCase;
import com.backend.immilog.user.domain.model.user.User;
import com.backend.immilog.user.domain.model.user.UserStatus;
import com.backend.immilog.user.exception.UserException;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.backend.immilog.user.exception.UserErrorCode.PASSWORD_NOT_MATCH;
import static com.backend.immilog.user.exception.UserErrorCode.USER_STATUS_NOT_ACTIVE;

@Service
public class UserSignInService implements UserSignInUseCase {
    private final UserQueryService userQueryService;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenCommandService refreshTokenCommandService;

    public UserSignInService(
            UserQueryService userQueryService,
            PasswordEncoder passwordEncoder,
            TokenProvider tokenProvider,
            RefreshTokenCommandService refreshTokenCommandService
    ) {
        this.userQueryService = userQueryService;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.refreshTokenCommandService = refreshTokenCommandService;
    }

    private final int REFRESH_TOKEN_EXPIRE_TIME = 5 * 29 * 24 * 60;
    private final String TOKEN_PREFIX = "Refresh: ";

    @Override
    public UserSignInResult signIn(
            UserSignInCommand command,
            CompletableFuture<Pair<String, String>> country
    ) {
        final User user = userQueryService.getUserByEmail(command.email());
        validatePassword(command.password(), user.password());
        validateUserStatus(user.userStatus());
        final String accessToken = tokenProvider.issueAccessToken(user.seq(), user.email(), user.userRole(), user.country());
        final String refreshToken = tokenProvider.issueRefreshToken();

        refreshTokenCommandService.saveKeyAndValue(
                TOKEN_PREFIX + refreshToken,
                user.email(),
                REFRESH_TOKEN_EXPIRE_TIME
        );

        Pair<String, String> countryAndRegionPair = fetchLocation(country);

        boolean locationMatch = isLocationMatch(user, countryAndRegionPair);

        return UserSignInResult.of(user, accessToken, refreshToken, locationMatch);
    }

    @Override
    public UserSignInResult getUserSignInDTO(
            Long userSeq,
            Pair<String, String> country
    ) {
        final User user = userQueryService.getUserById(userSeq);
        boolean isLocationMatch = isLocationMatch(user, country);
        final String accessToken = tokenProvider.issueAccessToken(user.seq(), user.email(), user.userRole(), user.country());
        final String refreshToken = tokenProvider.issueRefreshToken();

        refreshTokenCommandService.saveKeyAndValue(
                TOKEN_PREFIX + refreshToken,
                user.email(),
                REFRESH_TOKEN_EXPIRE_TIME
        );

        return UserSignInResult.of(user, accessToken, refreshToken, isLocationMatch);
    }


    private static Pair<String, String> fetchLocation(CompletableFuture<Pair<String, String>> country) {
        return country
                .orTimeout(5, TimeUnit.SECONDS) // 5초 이내에 완료되지 않으면 타임아웃
                .exceptionally(throwable -> Pair.of("Error", "Timeout"))
                .join();
    }

    private static boolean isLocationMatch(
            User user,
            Pair<String, String> countryPair
    ) {
        String country = user.countryNameInKorean();
        String region = user.region();
        return country.equals(countryPair.getFirst()) && region.equals(countryPair.getSecond());
    }

    private static void validateUserStatus(UserStatus status) {
        if (!status.equals(UserStatus.ACTIVE)) {
            throw new UserException(USER_STATUS_NOT_ACTIVE);
        }
    }

    private void validatePassword(
            String requestedPassword,
            String userPassword
    ) {
        if (!passwordEncoder.matches(requestedPassword, userPassword)) {
            throw new UserException(PASSWORD_NOT_MATCH);
        }
    }
}
