package com.backend.immilog.user.infrastructure.jpa.entity;

import com.backend.immilog.global.enums.UserRole;
import com.backend.immilog.global.enums.Country;
import com.backend.immilog.user.domain.enums.UserStatus;
import com.backend.immilog.user.domain.model.user.*;
import com.backend.immilog.user.exception.UserException;
import com.backend.immilog.user.infrastructure.jpa.entity.user.UserEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("UserEntity 테스트")
class UserEntityTest {
    @Test
    @DisplayName("UserEntity from User - valid User object")
    void userEntityFromUser_validUser() {
        String mail = "test@user.com";
        User user = new User(
                1L,
                Auth.of(mail, "password"),
                UserRole.ROLE_USER,
                new ReportData(1L, Date.valueOf("2024-12-12")),
                Profile.of("TestUser", "image.png", Country.SOUTH_KOREA),
                Location.of(Country.SOUTH_KOREA, "Country"),
                UserStatus.ACTIVE,
                null
        );

        UserEntity userEntity = UserEntity.from(user);
        User domain = userEntity.toDomain();

        assertThat(domain.nickname()).isEqualTo(user.nickname());
        assertThat(domain.email()).isEqualTo(user.email());
        assertThat(domain.password()).isEqualTo(user.password());
        assertThat(domain.imageUrl()).isEqualTo(user.imageUrl());
        assertThat(domain.userStatus()).isEqualTo(user.userStatus());
        assertThat(domain.userRole()).isEqualTo(user.userRole());
        assertThat(domain.interestCountry()).isEqualTo(user.interestCountry());
        assertThat(domain.location()).isEqualTo(user.location());
        assertThat(domain.reportData()).isEqualTo(user.reportData());
    }

    @Test
    @DisplayName("UserEntity toDomain - valid UserEntity object")
    void userEntityToDomain_validUserEntity() {
        Location country = Location.of(Country.SOUTH_KOREA, "Country");
        ReportData repost = new ReportData(1L, Date.valueOf("2024-12-12"));

        String mail = "test@user.com";
        User model = new User(
                1L,
                Auth.of(mail, "password"),
                UserRole.ROLE_USER,
                repost,
                Profile.of("TestUser", "image.png", Country.SOUTH_KOREA),
                country,
                UserStatus.ACTIVE,
                null
        );
        UserEntity userEntity = UserEntity.from(model);
        User user = userEntity.toDomain();

        assertThat(user.nickname()).isEqualTo("TestUser");
        assertThat(user.email()).isEqualTo("test@user.com");
        assertThat(user.password()).isEqualTo("password");
        assertThat(user.imageUrl()).isEqualTo("image.png");
        assertThat(user.userStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(user.userRole()).isEqualTo(UserRole.ROLE_USER);
        assertThat(user.interestCountry()).isEqualTo(Country.SOUTH_KOREA);
        assertThat(user.country()).isEqualTo(country.country());
        assertThat(user.reportData()).isEqualTo(repost);
    }

    @Test
    @DisplayName("UserEntity from User - null User")
    void userEntityFromUser_nullUser() {
        User user = null;
        assertThatThrownBy(() -> UserEntity.from(null)).isInstanceOf(NullPointerException.class);
    }
}