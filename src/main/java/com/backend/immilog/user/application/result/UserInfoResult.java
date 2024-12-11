package com.backend.immilog.user.application.result;

import com.backend.immilog.global.enums.UserRole;
import com.backend.immilog.user.domain.enums.UserCountry;
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
        UserCountry country,
        UserCountry interestCountry,
        String region,
        UserRole userRole,
        UserStatus userStatus
) {
    public static UserInfoResult from(
            User user
    ) {
        return UserInfoResult.builder()
                .seq(user.getSeq())
                .nickName(user.getNickName())
                .email(user.getEmail())
                .profileImage(user.getImageUrl())
                .reportedCount(user.getReportedCount())
                .reportedDate(user.getReportedDate())
                .country(user.getCountry())
                .region(user.getRegion())
                .interestCountry(user.getInterestCountry())
                .userRole(user.getUserRole())
                .userStatus(user.getUserStatus())
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
