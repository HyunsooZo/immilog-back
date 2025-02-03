package com.backend.immilog.user.application.result;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.global.enums.UserRole;
import com.backend.immilog.user.domain.enums.UserStatus;
import com.backend.immilog.user.domain.model.user.User;

import java.sql.Date;

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
    public static UserInfoResult from(User user) {
        return new UserInfoResult(
                user.seq(),
                user.nickname(),
                user.email(),
                user.imageUrl(),
                user.reportedCount(),
                user.reportedDate(),
                user.country(),
                user.interestCountry(),
                user.region(),
                user.userRole(),
                user.userStatus()
        );
    }

    public static UserInfoResult from(
            Long seq,
            String nickName,
            String profileImage
    ) {
        return new UserInfoResult(
                seq,
                nickName,
                null,
                profileImage,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }
}
