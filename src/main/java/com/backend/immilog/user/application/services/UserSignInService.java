package com.backend.immilog.user.application.services;

import com.backend.immilog.global.application.RedisService;
import com.backend.immilog.global.security.TokenProvider;
import com.backend.immilog.user.application.command.UserSignInCommand;
import com.backend.immilog.user.application.result.UserSignInResult;
import com.backend.immilog.user.application.services.query.UserQueryService;
import com.backend.immilog.user.domain.enums.UserStatus;
import com.backend.immilog.user.domain.model.user.User;
import com.backend.immilog.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.backend.immilog.user.exception.UserErrorCode.*;

@RequiredArgsConstructor
@Service
public class UserSignInService {
    private final UserQueryService userQueryService;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RedisService redisService;

    final int REFRESH_TOKEN_EXPIRE_TIME = 5 * 29 * 24 * 60;
    final String TOKEN_PREFIX = "Refresh: ";

    public UserSignInResult signIn(
            UserSignInCommand userSignInCommand,
            CompletableFuture<Pair<String, String>> country
    ) {
        User user = getUser(userSignInCommand.email());
        validateIfPasswordsMatches(userSignInCommand, user);
        validateIfUserStateIsActive(user);
        String accessToken = tokenProvider.issueAccessToken(
                user.getSeq(),
                user.getEmail(),
                user.getUserRole(),
                user.getCountry().toGlobalCountries()
        );

        String refreshToken = tokenProvider.issueRefreshToken();

        redisService.saveKeyAndValue(
                TOKEN_PREFIX + refreshToken,
                user.getEmail(),
                REFRESH_TOKEN_EXPIRE_TIME
        );

        Pair<String, String> countryAndRegionPair = fetchLocation(country);

        return UserSignInResult.of(
                user,
                accessToken,
                refreshToken,
                isLocationMatch(user, countryAndRegionPair)
        );
    }

    public UserSignInResult getUserSignInDTO(
            Long userSeq,
            Pair<String, String> country
    ) {
        final User user = getUser(userSeq);
        boolean isLocationMatch = isLocationMatch(user, country);
        final String accessToken = tokenProvider.issueAccessToken(
                user.getSeq(),
                user.getEmail(),
                user.getUserRole(),
                user.getCountry().toGlobalCountries()
        );
        final String refreshToken = tokenProvider.issueRefreshToken();

        redisService.saveKeyAndValue(
                TOKEN_PREFIX + refreshToken,
                user.getEmail(),
                REFRESH_TOKEN_EXPIRE_TIME
        );

        return UserSignInResult.of(
                user,
                accessToken,
                refreshToken,
                isLocationMatch
        );
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
        String country = user.getCountry().getCountryKoreanName();
        String region = user.getRegion();
        return country.equals(countryPair.getFirst()) && region.equals(countryPair.getSecond());
    }

    private static void validateIfUserStateIsActive(User user) {
        if (!user.getUserStatus().equals(UserStatus.ACTIVE)) {
            throw new UserException(USER_STATUS_NOT_ACTIVE);
        }
    }

    private void validateIfPasswordsMatches(
            UserSignInCommand userSignInCommand,
            User user
    ) {
        if (!passwordEncoder.matches(userSignInCommand.password(), user.getPassword())) {
            throw new UserException(PASSWORD_NOT_MATCH);
        }
    }

    private User getUser(String email) {
        return userQueryService
                .getUserByEmail(email)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    }

    private User getUser(Long id) {
        return userQueryService
                .getUserById(id)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    }
}
