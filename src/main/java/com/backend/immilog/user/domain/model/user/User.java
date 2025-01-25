package com.backend.immilog.user.domain.model.user;

import com.backend.immilog.global.enums.UserRole;
import com.backend.immilog.user.application.command.UserSignUpCommand;
import com.backend.immilog.user.domain.enums.UserCountry;
import com.backend.immilog.user.domain.enums.UserStatus;
import lombok.Builder;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public class User {
    private final Long seq;
    private Auth auth;
    private final UserRole userRole;
    private ReportData reportData;
    private Profile profile;
    private Location location;
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
                    this.auth = Auth.of(this.auth.email(), encodedPassword);
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
                    this.profile = Profile.of(nickname, this.profile.imageUrl(), this.profile.interestCountry());
                    updateTimestamp();
                });
    }

    public void changeInterestCountry(UserCountry interestCountry) {
        Optional.ofNullable(interestCountry)
                .filter(country -> !country.equals(this.profile.interestCountry()))
                .ifPresent(country -> {
                    this.profile = Profile.of(this.profile.nickname(), this.profile.imageUrl(), country);
                    updateTimestamp();
                });
    }

    public void changeImageUrl(String imageUrl) {
        Optional.ofNullable(imageUrl)
                .filter(url -> !url.trim().isEmpty())
                .ifPresent(url -> {

                    updateTimestamp();
                });
    }

    public void changeRegion(String second) {
        Optional.ofNullable(second)
                .filter(region -> !region.trim().isEmpty())
                .ifPresent(region -> {
                    this.location = Location.of(this.location.country(), region);
                    updateTimestamp();
                });
    }

    public void changeCountry(UserCountry country) {
        Optional.ofNullable(country)
                .filter(value -> !value.equals(this.location.country()))
                .ifPresent(value -> {
                    this.location = Location.of(value, this.location.region());
                    updateTimestamp();
                });
    }

    private void updateTimestamp() {
        if (this.seq != null) {
            this.updatedAt = LocalDateTime.now();
        }
    }

    public Long getReportedCount() {
        return this.reportData.reportedCount();
    }

    public Date getReportedDate() {
        return this.reportData.reportedDate();
    }

    public UserCountry getCountry() {
        return this.location.country();
    }

    public String getCountryName() {
        return this.location.country().name();
    }

    public String getCountryNameInKorean() {
        return this.location.country().koreanName();
    }

    public String getRegion() {
        return this.location.region();
    }

    public void increaseReportedCount() {
        Long newCount = this.reportData.reportedCount() + 1;
        this.reportData = ReportData.of(newCount, Date.valueOf(LocalDate.now()));
    }

    public boolean hasSameSeq(Long userSeq) {
        return this.seq.equals(userSeq);
    }

    public String getNickname() {
        return this.profile.nickname();
    }

    public String getImageUrl() {
        return this.profile.imageUrl();
    }

    public UserCountry getInterestCountry() {
        return this.profile.interestCountry();
    }

    public String getEmail() {
        return this.auth.email();
    }

    public String getPassword() {
        return this.auth.password();
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
