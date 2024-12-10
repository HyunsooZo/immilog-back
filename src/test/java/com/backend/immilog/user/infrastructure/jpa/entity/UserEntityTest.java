package com.backend.immilog.user.infrastructure.jpa.entity;

import com.backend.immilog.global.enums.UserRole;
import com.backend.immilog.user.domain.enums.UserCountry;
import com.backend.immilog.user.domain.enums.UserStatus;
import com.backend.immilog.user.domain.model.user.Location;
import com.backend.immilog.user.domain.model.user.ReportInfo;
import com.backend.immilog.user.domain.model.user.User;
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
                .nickName("TestUser")
                .email("test@user.com")
                .password("password")
                .imageUrl("image.png")
                .userStatus(UserStatus.ACTIVE)
                .userRole(UserRole.ROLE_USER)
                .interestCountry(UserCountry.SOUTH_KOREA)
                .location(Location.builder().country(UserCountry.SOUTH_KOREA).region("Country").build())
                .reportInfo(ReportInfo.builder().reportedCount(1L).reportedDate(Date.valueOf("2024-12-12")).build())
                .build();

        UserEntity userEntity = UserEntity.from(user);

        assertThat(userEntity.getNickName()).isEqualTo(user.getNickName());
        assertThat(userEntity.getEmail()).isEqualTo(user.getEmail());
        assertThat(userEntity.getPassword()).isEqualTo(user.getPassword());
        assertThat(userEntity.getImageUrl()).isEqualTo(user.getImageUrl());
        assertThat(userEntity.getUserStatus()).isEqualTo(user.getUserStatus());
        assertThat(userEntity.getUserRole()).isEqualTo(user.getUserRole());
        assertThat(userEntity.getInterestCountry()).isEqualTo(user.getInterestCountry());
        assertThat(userEntity.getLocation()).isEqualTo(user.getLocation());
        assertThat(userEntity.getReportInfo()).isEqualTo(user.getReportInfo());
    }

    @Test
    @DisplayName("UserEntity toDomain - valid UserEntity object")
    void userEntityToDomain_validUserEntity() {
        UserEntity userEntity = UserEntity.builder()
                .seq(1L)
                .nickName("TestUser")
                .email("test@user.com")
                .password("password")
                .imageUrl("image.png")
                .userStatus(UserStatus.ACTIVE)
                .userRole(UserRole.ROLE_USER)
                .interestCountry(UserCountry.SOUTH_KOREA)
                .location(Location.builder().country(UserCountry.SOUTH_KOREA).region("Country").build())
                .reportInfo(ReportInfo.builder().reportedCount(1L).reportedDate(Date.valueOf("2024-12-12")).build())
                .build();
        User user = userEntity.toDomain();

        assertThat(user.getNickName()).isEqualTo(userEntity.getNickName());
        assertThat(user.getEmail()).isEqualTo(userEntity.getEmail());
        assertThat(user.getPassword()).isEqualTo(userEntity.getPassword());
        assertThat(user.getImageUrl()).isEqualTo(userEntity.getImageUrl());
        assertThat(user.getUserStatus()).isEqualTo(userEntity.getUserStatus());
        assertThat(user.getUserRole()).isEqualTo(userEntity.getUserRole());
        assertThat(user.getInterestCountry()).isEqualTo(userEntity.getInterestCountry());
        assertThat(user.getLocation()).isEqualTo(userEntity.getLocation());
        assertThat(user.getReportInfo()).isEqualTo(userEntity.getReportInfo());
    }

    @Test
    @DisplayName("UserEntity from User - null User")
    void userEntityFromUser_nullUser() {
        User user = null;

        assertThatThrownBy(() -> UserEntity.from(user))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("UserEntity toDomain - null")
    void userEntityToDomain_nullFields() {
        UserEntity userEntity = UserEntity.builder().build();
        User user = userEntity.toDomain();
        assertThat(user.getSeq()).isNull();
        assertThat(user.getNickName()).isNull();
        assertThat(user.getEmail()).isNull();
        assertThat(user.getPassword()).isNull();
        assertThat(user.getImageUrl()).isNull();
        assertThat(user.getUserStatus()).isNull();
        assertThat(user.getUserRole()).isNull();
        assertThat(user.getInterestCountry()).isNull();
        assertThat(user.getLocation()).isNull();
        assertThat(user.getReportInfo()).isNull();
    }
}