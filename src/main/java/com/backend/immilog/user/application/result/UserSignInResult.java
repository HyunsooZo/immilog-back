package com.backend.immilog.user.application.result;

import com.backend.immilog.user.domain.model.user.User;
import lombok.Builder;

@Builder
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

        return UserSignInResult.builder()
                .userSeq(user.seq())
                .email(user.email())
                .nickname(user.nickname())
                .accessToken(accessToken == null ? "" : accessToken)
                .refreshToken(refreshToken == null ? "" : refreshToken)
                .country(user.countryName())
                .interestCountry(interestCountry)
                .region(user.region())
                .userProfileUrl(user.imageUrl())
                .isLocationMatch(isLocationMatch)
                .build();
    }
}

