package com.backend.immilog.user.domain.model.user;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.global.enums.UserRole;
import com.backend.immilog.user.exception.UserErrorCode;
import com.backend.immilog.user.exception.UserException;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class User {
    private final Long seq;
    private Auth auth;
    private final UserRole userRole;
    private ReportData reportData;
    private Profile profile;
    private Location location;
    private UserStatus userStatus;
    private LocalDateTime updatedAt;

    public User(
            Long seq,
            Auth auth,
            UserRole userRole,
            ReportData reportData,
            Profile profile,
            Location location,
            UserStatus userStatus,
            LocalDateTime updatedAt
    ) {
        this.seq = seq;
        this.auth = auth;
        this.userRole = userRole;
        this.reportData = reportData;
        this.profile = profile;
        this.location = location;
        this.userStatus = userStatus;
        this.updatedAt = updatedAt;
    }

    public static User of(
            Auth auth,
            Location location,
            Profile profile
    ) {
        return new User(
                null,
                auth,
                UserRole.ROLE_USER,
                new ReportData(0L, null),
                profile,
                location,
                UserStatus.PENDING,
                null
        );
    }

    public User changePassword(String encodedPassword) {
        if (encodedPassword == null || encodedPassword.trim().isEmpty()) {
            return this;
        }
        this.auth = Auth.of(this.auth.email(), encodedPassword);
        this.updatedAt = LocalDateTime.now();
        return this;
    }

    public User updateStatus(UserStatus userStatus) {
        if (userStatus == null || userStatus.equals(this.userStatus)) {
            return this;
        }
        this.userStatus = userStatus;
        this.updatedAt = LocalDateTime.now();
        return this;
    }

    public User updateNickname(String nickname) {
        if (nickname == null || nickname.trim().isEmpty() || nickname.equals(this.profile.nickname())) {
            return this;
        }
        this.profile = Profile.of(nickname, this.profile.imageUrl(), this.profile.interestCountry());
        this.updatedAt = LocalDateTime.now();
        return this;
    }

    public User updateInterestCountry(Country interestCountry) {
        if (interestCountry == null || interestCountry.equals(this.profile.interestCountry())) {
            return this;
        }
        this.profile = Profile.of(this.profile.nickname(), this.profile.imageUrl(), interestCountry);
        this.updatedAt = LocalDateTime.now();
        return this;
    }

    public User updateImageUrl(String imageUrl) {
        if (imageUrl.equals(this.profile.imageUrl())) {
            return this;
        }
        this.profile = Profile.of(this.profile.nickname(), imageUrl, this.profile.interestCountry());
        this.updatedAt = LocalDateTime.now();
        return this;
    }

    public User updateRegion(String region) {
        if (region == null || region.trim().isEmpty() || region.equals(this.location.region())) {
            return this;
        }
        this.location = Location.of(this.location.country(), region);
        this.updatedAt = LocalDateTime.now();
        return this;
    }

    public User updateCountry(Country country) {
        if (country == null || country.equals(this.location.country())) {
            return this;
        }
        this.location = Location.of(country, this.location.region());
        this.updatedAt = LocalDateTime.now();
        return this;
    }


    public User increaseReportedCount() {
        var newCount = this.reportData.reportedCount() + 1;
        this.reportData = ReportData.of(newCount, Date.valueOf(LocalDate.now()));
        this.updatedAt = LocalDateTime.now();
        return this;
    }

    public void validateAdmin() {
        if (!this.userRole.equals(UserRole.ROLE_ADMIN)) {
            throw new UserException(UserErrorCode.NOT_AN_ADMIN_USER);
        }
    }

    public Long reportedCount() {return this.reportData.reportedCount();}

    public Date reportedDate() {return this.reportData.reportedDate();}

    public Country country() {return this.location.country();}

    public String countryName() {return this.location.country().name();}

    public String countryNameInKorean() {return this.location.country().koreanName();}

    public String region() {return this.location.region();}

    public boolean hasSameSeq(Long userSeq) {return this.seq.equals(userSeq);}

    public String nickname() {return this.profile.nickname();}

    public String imageUrl() {return this.profile.imageUrl();}

    public Country interestCountry() {return this.profile.interestCountry();}

    public String email() {return this.auth.email();}

    public String password() {return this.auth.password();}

    public Long seq() {return seq;}

    public Auth auth() {return auth;}

    public UserRole userRole() {return userRole;}

    public ReportData reportData() {return reportData;}

    public Profile profile() {return profile;}

    public Location location() {return location;}

    public UserStatus userStatus() {return userStatus;}

    public LocalDateTime updatedAt() {return updatedAt;}

}
