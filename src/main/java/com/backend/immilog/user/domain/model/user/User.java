package com.backend.immilog.user.domain.model.user;

import com.backend.immilog.global.enums.UserRole;
import com.backend.immilog.user.application.command.UserSignUpCommand;
import com.backend.immilog.user.domain.enums.UserCountry;
import com.backend.immilog.user.domain.enums.UserStatus;
import lombok.Builder;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Optional;

public class User {
    private final Long seq;
    private final Auth auth;
    private final UserRole userRole;
    private final ReportData reportData;
    private final Profile profile;
    private final Location location;
    private UserStatus userStatus;
    private LocalDateTime updatedAt;

    @Builder
    private User(
            Long seq,
            Auth auth,
            UserStatus userStatus,
            UserRole userRole,
            Location location,
            ReportData reportData,
            Profile profile,
            LocalDateTime updatedAt
    ) {
        this.seq = seq;
        this.auth = auth;
        this.userStatus = userStatus;
        this.userRole = userRole;
        this.location = location;
        this.reportData = reportData;
        this.profile = profile;
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
        Auth auth = Auth.of(command.email(), encodedPassword);
        Location location = Location.of(country, command.region());
        Profile profile = Profile.of(command.nickName(), command.profileImage(), interestCountry);
        return new User(
                null,
                auth,
                UserStatus.PENDING,
                UserRole.ROLE_USER,
                location,
                new ReportData(0L, null),
                profile,
                null
        );
    }

    public void changePassword(String encodedPassword) {
        Optional.ofNullable(encodedPassword)
                .filter(password -> !password.trim().isEmpty())
                .ifPresent(password -> {
                    this.auth.updatePassword(password);
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
                    this.profile.updateNickName(name);
                    updateTimestamp();
                });
    }

    public void changeInterestCountry(UserCountry interestCountry) {
        Optional.ofNullable(interestCountry)
                .filter(country -> !country.equals(this.profile.getInterestCountry()))
                .ifPresent(country -> {
                    this.profile.updateInterestCountry(country);
                    updateTimestamp();
                });
    }

    public void changeImageUrl(String imageUrl) {
        Optional.ofNullable(imageUrl)
                .filter(url -> !url.trim().isEmpty())
                .ifPresent(url -> {
                    this.profile.updateImageUrl(url);
                    updateTimestamp();
                });
    }

    public void changeRegion(String second) {
        Optional.ofNullable(second)
                .filter(region -> !region.trim().isEmpty())
                .ifPresent(region -> {
                    this.location.updateLocation(this.location.getCountry(), region);
                    updateTimestamp();
                });
    }

    public void changeCountry(UserCountry country) {
        Optional.ofNullable(country)
                .filter(value -> !value.equals(this.location.getCountry()))
                .ifPresent(value -> {
                    this.location.updateLocation(value, this.location.getRegion());
                    updateTimestamp();
                });
    }

    private void updateTimestamp() {
        if (this.seq != null) {
            this.updatedAt = LocalDateTime.now();
        }
    }

    public Long getReportedCount() {
        return this.reportData.getReportedCount();
    }

    public Date getReportedDate() {
        return this.reportData.getReportedDate();
    }

    public UserCountry getCountry() {
        return this.location.getCountry();
    }

    public String getCountryName() {
        return this.location.getCountry().name();
    }

    public String getCountryNameInKorean() {
        return this.location.getCountry().koreanName();
    }

    public String getRegion() {
        return this.location.getRegion();
    }

    public void increaseReportedCount() {
        this.reportData.increaseReportCount();
        this.reportData.updateReportedDate();
    }

    public boolean hasSameSeq(Long userSeq) {
        return this.seq.equals(userSeq);
    }

    public String getNickname() {
        return this.profile.getNickname();
    }

    public String getImageUrl() {
        return this.profile.getImageUrl();
    }

    public UserCountry getInterestCountry() {
        return this.profile.getInterestCountry();
    }

    public String getEmail() {
        return this.auth.getEmail();
    }

    public String getPassword() {
        return this.auth.getPassword();
    }

    public Long getSeq() {
        return this.seq;
    }

    public UserRole getUserRole() {
        return this.userRole;
    }

    public UserStatus getUserStatus() {
        return this.userStatus;
    }

    public Location getLocation() {
        return this.location;
    }

    public ReportData getReportData() {
        return this.reportData;
    }

    public Profile getProfile() {
        return this.profile;
    }
}
