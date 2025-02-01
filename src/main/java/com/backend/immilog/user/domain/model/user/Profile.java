package com.backend.immilog.user.domain.model.user;

import com.backend.immilog.global.enums.Country;

public record Profile(
        String nickname,
        String imageUrl,
        Country interestCountry
) {
    public static Profile of(
            String nickname,
            String imageUrl,
            Country interestCountry
    ) {
        return new Profile(nickname, imageUrl, interestCountry);
    }
}
