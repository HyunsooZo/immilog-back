package com.backend.immilog.user.application.usecase;

import com.backend.immilog.user.application.command.UserSignInCommand;
import com.backend.immilog.user.application.result.LocationResult;
import com.backend.immilog.user.application.result.UserSignInResult;
import com.backend.immilog.user.application.services.RefreshTokenCommandService;
import com.backend.immilog.user.application.services.UserQueryService;
import com.backend.immilog.user.domain.service.UserPasswordPolicy;
import com.backend.immilog.user.domain.service.UserTokenGenerator;
import com.backend.immilog.user.domain.service.UserValidator;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public interface UserSignInUseCase {
    UserSignInResult signIn(
            UserSignInCommand command,
            CompletableFuture<LocationResult> country
    );

    UserSignInResult getUserSignInDTO(
            Long userSeq,
            LocationResult country
    );

    @Service
    class UserLoginProcessor implements UserSignInUseCase {
        private final UserQueryService userQueryService;
        private final RefreshTokenCommandService refreshTokenCommandService;
        private final UserTokenGenerator userTokenGenerator;
        private final UserPasswordPolicy userPasswordPolicy;
        private final UserValidator userValidator;

        public UserLoginProcessor(
                UserQueryService userQueryService,
                RefreshTokenCommandService refreshTokenCommandService,
                UserTokenGenerator tokenProvider,
                UserPasswordPolicy userPasswordPolicy,
                UserValidator userValidator
        ) {
            this.userQueryService = userQueryService;
            this.refreshTokenCommandService = refreshTokenCommandService;
            this.userTokenGenerator = tokenProvider;
            this.userPasswordPolicy = userPasswordPolicy;
            this.userValidator = userValidator;
        }

        private final int REFRESH_TOKEN_EXPIRE_TIME = 5 * 29 * 24 * 60;
        private final String TOKEN_PREFIX = "Refresh: ";

        @Override
        public UserSignInResult signIn(
                UserSignInCommand command,
                CompletableFuture<LocationResult> country
        ) {
            final var user = userQueryService.getUserByEmail(command.email());
            userPasswordPolicy.validate(command.password(), user.password());
            userValidator.validateUserStatus(user.userStatus());
            final var accessToken = userTokenGenerator.generate(user.seq(), user.email(), user.userRole(), user.country());
            final var refreshToken = userTokenGenerator.generateRefreshToken();

            refreshTokenCommandService.saveKeyAndValue(TOKEN_PREFIX + refreshToken, user.email(), REFRESH_TOKEN_EXPIRE_TIME);
            var locationResult = country.orTimeout(5, TimeUnit.SECONDS).exceptionally(throwable -> new LocationResult("Error", "Timeout")).join();
            boolean locationMatch = user.countryNameInKorean().equals(locationResult.country()) && user.region().equals(locationResult.city());

            return UserSignInResult.of(user, accessToken, refreshToken, locationMatch);
        }

        @Override
        public UserSignInResult getUserSignInDTO(
                Long userSeq,
                LocationResult country
        ) {
            final var user = userQueryService.getUserById(userSeq);
            boolean isLocationMatch = user.countryNameInKorean().equals(country.country()) && user.region().equals(country.city());
            final var accessToken = userTokenGenerator.generate(user.seq(), user.email(), user.userRole(), user.country());
            final var refreshToken = userTokenGenerator.generateRefreshToken();

            refreshTokenCommandService.saveKeyAndValue(TOKEN_PREFIX + refreshToken, user.email(), REFRESH_TOKEN_EXPIRE_TIME);

            return UserSignInResult.of(user, accessToken, refreshToken, isLocationMatch);
        }

    }
}
