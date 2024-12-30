package com.backend.immilog.user.domain.model.user;

import com.backend.immilog.global.enums.UserRole;
import com.backend.immilog.user.application.command.UserSignUpCommand;
import com.backend.immilog.user.domain.enums.UserCountry;
import com.backend.immilog.user.domain.enums.UserStatus;
import lombok.Builder;
import lombok.Getter;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Optional;

@Getter
public class User {
    private final Long seq;
    private final String email;
    private final UserRole userRole;
    private final ReportInfo reportInfo;
    private final LocalDateTime createdAt;
    private String nickname;
    private String password;
    private String imageUrl;
    private UserStatus userStatus;
    private UserCountry interestCountry;
    private Location location;
    private LocalDateTime updatedAt;

    @Builder
    private User(
            Long seq,
            String nickName,
            String email,
            String password,
            String imageUrl,
            UserStatus userStatus,
            UserRole userRole,
            UserCountry interestCountry,
            Location location,
            ReportInfo reportInfo,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.seq = seq;
        this.nickname = nickName;
        this.email = email;
        this.password = password;
        this.imageUrl = imageUrl;
        this.userStatus = userStatus;
        this.userRole = userRole;
        this.interestCountry = interestCountry;
        this.location = location;
        this.reportInfo = reportInfo;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static User of(
            UserSignUpCommand command,
            String encodedPassword
    ) {
        final UserCountry country = UserCountry.valueOf(command.country());
        final String interestCountryValue = command.interestCountry();
        final boolean isInterestCountryNull = interestCountryValue == null || interestCountryValue.isEmpty();
        final UserCountry interestCountry = isInterestCountryNull ? null : country;
        return new User(
                null,
                command.nickName(),
                command.email(),
                encodedPassword,
                command.profileImage(),
                UserStatus.PENDING,
                UserRole.ROLE_USER,
                interestCountry,
                Location.of(country, command.region()),
                new ReportInfo(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public void changePassword(String encodedPassword) {
        Optional.ofNullable(encodedPassword)
                .filter(password -> !password.trim().isEmpty())
                .ifPresent(password -> {
                    this.password = password;
                    updateTimestamp();
                });
    }

    public void changeUserStatus(UserStatus userStatus) {
        Optional.ofNullable(userStatus)
                .filter(status -> !status.equals(this.userStatus))
                .ifPresent(status -> {
                    this.userStatus = status;
                    updateTimestamp();
                });
    }

    public void changeNickname(String nickname) {
        Optional.ofNullable(nickname)
                .filter(name -> !name.trim().isEmpty())
                .ifPresent(name -> {
                    this.nickname = name;
                    updateTimestamp();
                });
    }

    public void changeInterestCountry(UserCountry interestCountry) {
        Optional.ofNullable(interestCountry)
                .filter(country -> !country.equals(this.interestCountry))
                .ifPresent(country -> {
                    this.interestCountry = country;
                    updateTimestamp();
                });
    }

    public void changeImageUrl(String imageUrl) {
        Optional.ofNullable(imageUrl)
                .filter(url -> !url.trim().isEmpty())
                .ifPresent(url -> {
                    this.imageUrl = url;
                    updateTimestamp();
                });
    }

    public void changeRegion(String second) {
        Optional.ofNullable(second)
                .filter(region -> !region.trim().isEmpty())
                .ifPresent(region -> {
                    this.location = Location.of(this.location.getCountry(), region);
                    updateTimestamp();
                });
    }

    public void changeCountry(UserCountry country) {
        Optional.ofNullable(country)
                .filter(value -> !value.equals(this.location.getCountry()))
                .ifPresent(value -> {
                    this.location = Location.of(value, this.location.getRegion());
                    updateTimestamp();
                });
    }

    private void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getReportedCount() {
        return this.reportInfo.getReportedCount();
    }

    public Date getReportedDate() {
        return this.reportInfo.getReportedDate();
    }

    public UserCountry getCountry() {
        return this.location.getCountry();
    }

    public String getRegion() {
        return this.location.getRegion();
    }

    public void increaseReportedCount() {
        this.reportInfo.increaseReportCount();
        this.reportInfo.updateReportedDate();
    }

    public boolean hasSameSeq(Long userSeq) {
        return this.seq.equals(userSeq);
    }
}
