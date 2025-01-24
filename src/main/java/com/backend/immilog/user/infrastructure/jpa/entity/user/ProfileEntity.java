package com.backend.immilog.user.infrastructure.jpa.entity.user;

import com.backend.immilog.user.domain.enums.UserCountry;
import com.backend.immilog.user.domain.model.user.Profile;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
@Embeddable
public class ProfileEntity {

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "image_url")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "interest_country")
    private UserCountry interestCountry;

    protected ProfileEntity() {}

    protected ProfileEntity(
            String nickname,
            String imageUrl,
            UserCountry interestCountry
    ) {
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.interestCountry = interestCountry;
    }

    public static ProfileEntity of(
            String nickname,
            String imageUrl,
            UserCountry interestCountry
    ) {
        return new ProfileEntity(nickname, imageUrl, interestCountry);
    }

    public Profile toDomain() {
        return Profile.of(this.nickname, this.imageUrl, this.interestCountry);
    }
}
