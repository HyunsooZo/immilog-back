package com.backend.immilog.user.domain.model.user;

import com.backend.immilog.user.domain.enums.UserCountry;
import lombok.Getter;

@Getter
public class Profile {
    private String nickname;
    private String imageUrl;
    private UserCountry interestCountry;

    protected Profile(
            String nickname,
            String imageUrl,
            UserCountry interestCountry
    ) {
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.interestCountry = interestCountry;
    }

    public static Profile of(
            String nickname,
            String imageUrl,
            UserCountry interestCountry
    ) {
        return new Profile(nickname, imageUrl, interestCountry);
    }

    protected void updateNickName(String name) {
        this.nickname = name;
    }

    protected void updateInterestCountry(UserCountry country) {
        this.interestCountry = country;
    }

    protected void updateImageUrl(String url) {
        this.imageUrl = url;
    }

}
