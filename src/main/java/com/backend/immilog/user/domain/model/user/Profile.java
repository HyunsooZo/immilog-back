package com.backend.immilog.user.domain.model.user;

import com.backend.immilog.user.domain.enums.UserCountry;

public record Profile(
        String nickname,
        String imageUrl,
        UserCountry interestCountry
) {
    public static Profile of(
            String nickname,
            String imageUrl,
            UserCountry interestCountry
    ) {
        return new Profile(nickname, imageUrl, interestCountry);
    }
}
