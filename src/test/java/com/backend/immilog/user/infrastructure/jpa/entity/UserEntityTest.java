package com.backend.immilog.user.infrastructure.jpa.entity;

import com.backend.immilog.global.enums.UserRole;
import com.backend.immilog.user.domain.enums.UserCountry;
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
        User user = User.builder()
                .seq(1L)
                .profile(Profile.of("TestUser", "image.png", UserCountry.SOUTH_KOREA))
                .auth(Auth.of("test@user.com", "password"))
                .userStatus(UserStatus.ACTIVE)
                .userRole(UserRole.ROLE_USER)
                .location(Location.of(UserCountry.SOUTH_KOREA, "Country"))
                .reportData(new ReportData(1L, Date.valueOf("2024-12-12")))
                .build();

        UserEntity userEntity = UserEntity.from(user);
        User domain = userEntity.toDomain();

        assertThat(domain.getNickname()).isEqualTo(user.getNickname());
        assertThat(domain.getEmail()).isEqualTo(user.getEmail());
        assertThat(domain.getPassword()).isEqualTo(user.getPassword());
        assertThat(domain.getImageUrl()).isEqualTo(user.getImageUrl());
        assertThat(domain.getUserStatus()).isEqualTo(user.getUserStatus());
        assertThat(domain.getUserRole()).isEqualTo(user.getUserRole());
        assertThat(domain.getInterestCountry()).isEqualTo(user.getInterestCountry());
        assertThat(domain.getLocation()).isEqualTo(user.getLocation());
        assertThat(domain.getReportData()).isEqualTo(user.getReportData());
    }

    @Test
    @DisplayName("UserEntity toDomain - valid UserEntity object")
    void userEntityToDomain_validUserEntity() {
        Location country = Location.of(UserCountry.SOUTH_KOREA, "Country");
        ReportData repost = new ReportData(1L, Date.valueOf("2024-12-12"));
        //return UserEntity.builder()
        //                .seq(user.getSeq())
        //                .email(user.getEmail())
        //                .password(user.getPassword())
        //                .userStatus(user.getUserStatus())
        //                .userRole(user.getUserRole())
        //                .country(user.getCountry())
        //                .region(user.getRegion())
        //                .reportedCount(user.getReportedCount())
        //                .reportedDate(user.getReportedDate())
        //                .nickname(user.getNickname())
        //                .imageUrl(user.getImageUrl())
        //                .interestCountry(user.getInterestCountry())
        //                .updatedAt(user.getSeq() != null ? LocalDateTime.now() : null)
        //                .build();
        UserEntity userEntity = UserEntity.builder()
                .seq(1L)
                .email("test@user.com")
                .password("password")
                .userStatus(UserStatus.ACTIVE)
                .userRole(UserRole.ROLE_USER)
                .country(UserCountry.SOUTH_KOREA)
                .region("Country")
                .reportedCount(1L)
                .reportedDate(Date.valueOf("2024-12-12"))
                .nickname("TestUser")
                .imageUrl("image.png")
                .interestCountry(UserCountry.SOUTH_KOREA)
                .updatedAt(null)
                .build();
        User user = userEntity.toDomain();

        assertThat(user.getNickname()).isEqualTo("TestUser");
        assertThat(user.getEmail()).isEqualTo("test@user.com");
        assertThat(user.getPassword()).isEqualTo("password");
        assertThat(user.getImageUrl()).isEqualTo("image.png");
        assertThat(user.getUserStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(user.getUserRole()).isEqualTo(UserRole.ROLE_USER);
        assertThat(user.getInterestCountry()).isEqualTo(UserCountry.SOUTH_KOREA);
        assertThat(user.getCountry()).isEqualTo(country.country());
        assertThat(user.getReportData()).isEqualTo(repost);
    }

    @Test
    @DisplayName("UserEntity from User - null User")
    void userEntityFromUser_nullUser() {
        User user = null;

        assertThatThrownBy(() -> UserEntity.from(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("UserEntity toDomain - null")
    void userEntityToDomain_nullFields() {
        Assertions.assertThatThrownBy(UserEntity.builder().build()::toDomain)
                .isInstanceOf(UserException.class);
    }
}