package com.backend.immilog.user.application.result;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.global.enums.UserRole;
import com.backend.immilog.user.domain.enums.UserStatus;
import com.backend.immilog.user.domain.model.user.User;
import lombok.Builder;

import java.sql.Date;

@Builder
public record UserInfoResult(
        Long seq,
        String nickName,
        String email,
        String profileImage,
        Long reportedCount,
        Date reportedDate,
        Country country,
        Country interestCountry,
        String region,
        UserRole userRole,
        UserStatus userStatus
) {
    public static UserInfoResult from(
            User user
    ) {
        return UserInfoResult.builder()
                .seq(user.seq())
                .nickName(user.nickname())
                .email(user.email())
                .profileImage(user.imageUrl())
                .reportedCount(user.reportedCount())
                .reportedDate(user.reportedDate())
                .country(user.country())
                .region(user.region())
                .interestCountry(user.interestCountry())
                .userRole(user.userRole())
                .userStatus(user.userStatus())
                .build();
    }

    public static UserInfoResult from(
            Long seq,
            String nickName,
            String profileImage
    ) {
        return UserInfoResult.builder()
                .seq(seq)
                .nickName(nickName)
                .profileImage(profileImage)
                .build();
    }
}
