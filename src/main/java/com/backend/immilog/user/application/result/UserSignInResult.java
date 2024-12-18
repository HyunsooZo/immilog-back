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
        String interestCountry = user.getInterestCountry() == null ?
                null : user.getCountry().name();

        return UserSignInResult.builder()
                .userSeq(user.getSeq())
                .email(user.getEmail())
                .nickname(user.getNickName())
                .accessToken(accessToken == null ? "" : accessToken)
                .refreshToken(refreshToken == null ? "" : refreshToken)
                .country(user.getCountry().name())
                .interestCountry(interestCountry)
                .region(user.getRegion())
                .userProfileUrl(user.getImageUrl())
                .isLocationMatch(isLocationMatch)
                .build();
    }
}

