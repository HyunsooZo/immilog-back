package com.backend.immilog.user.application.command;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.user.domain.model.user.UserStatus;

public record UserInfoUpdateCommand(
        String nickName,
        String profileImage,
        Country country,
        Country interestCountry,
        Double latitude,
        Double longitude,
        UserStatus status
) {
}
