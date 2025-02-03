package com.backend.immilog.user.application.result;

import com.backend.immilog.user.domain.model.user.User;

public record UserSignInResult(
        Long userSeq,
        String email,
        String nickname,
        String accessToken,
        String refreshToken,
        String country,
        String interestCountry,
        String region,
        String userProfileUrl,
        Boolean isLocationMatch
) {
    public static UserSignInResult of(
            User user,
            String accessToken,
            String refreshToken,
            boolean isLocationMatch
    ) {
        String interestCountry = user.interestCountry() == null ? null : user.countryName();

        return new UserSignInResult(
                user.seq(),
                user.email(),
                user.nickname(),
                accessToken == null ? "" : accessToken,
                refreshToken == null ? "" : refreshToken,
                user.countryName(),
                interestCountry,
                user.region(),
                user.imageUrl(),
                isLocationMatch
        );
    }
}

