package com.backend.immilog.user.infrastructure.jpa;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.user.domain.model.user.Profile;
import com.backend.immilog.user.exception.UserErrorCode;
import com.backend.immilog.user.exception.UserException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
@Embeddable
public class ProfileJpaValue {

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "image_url")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "interest_country")
    private Country interestCountry;

    protected ProfileJpaValue() {}

    protected ProfileJpaValue(
            String nickname,
            String imageUrl,
            Country interestCountry
    ) {
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.interestCountry = interestCountry;
    }

    public static ProfileJpaValue of(
            String nickname,
            String imageUrl,
            Country interestCountry
    ) {
        return new ProfileJpaValue(nickname, imageUrl, interestCountry);
    }

    public Profile toDomain() {
        if (this.nickname == null) {
            throw new UserException(UserErrorCode.ENTITY_TO_DOMAIN_ERROR);
        }
        return Profile.of(this.nickname, this.imageUrl, this.interestCountry);
    }
}
