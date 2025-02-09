package com.backend.immilog.user.application.result;

import com.backend.immilog.user.domain.model.user.User;
import com.backend.immilog.user.presentation.response.UserSignInResponse;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserSignInResult(
        @Schema(description = "사용자 식별자", example = "1") Long userSeq,
        @Schema(description = "이메일", example = "email@email.com") String email,
        @Schema(description = "닉네임", example = "nickname") String nickname,
        @Schema(description = "액세스 토큰", example = "access token") String accessToken,
        @Schema(description = "리프레시 토큰", example = "refresh token") String refreshToken,
        @Schema(description = "국가", example = "Korea") String country,
        @Schema(description = "관심 국가", example = "Korea") String interestCountry,
        @Schema(description = "지역", example = "Seoul") String region,
        @Schema(description = "프로필 이미지 URL", example = "profile image url") String userProfileUrl,
        @Schema(description = "위치 일치 여부", example = "true") Boolean isLocationMatch
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

    public UserSignInResponse toResponse() {
        return new UserSignInResponse(
                200,
                "success",
                this
        );
    }
}

