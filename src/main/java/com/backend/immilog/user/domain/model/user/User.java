package com.backend.immilog.user.domain.model.user;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.global.enums.UserRole;
import com.backend.immilog.user.domain.enums.UserStatus;
import com.backend.immilog.user.exception.UserErrorCode;
import com.backend.immilog.user.exception.UserException;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record User(
        Long seq,
        Auth auth,
        UserRole userRole,
        ReportData reportData,
        Profile profile,
        Location location,
        UserStatus userStatus,
        LocalDateTime updatedAt
) {

    public static User of(
            Auth auth,
            Location location,
            Profile profile
    ) {
        ReportData reportData = new ReportData(0L, null);
        return new User(
                null,
                auth,
                UserRole.ROLE_USER,
                reportData,
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
        Auth newAuth = Auth.of(this.auth.email(), encodedPassword);
        return new User(
                this.seq,
                newAuth,
                this.userRole,
                this.reportData,
                this.profile,
                this.location,
                this.userStatus,
                LocalDateTime.now()
        );
    }

    public User updateStatus(UserStatus userStatus) {
        if (userStatus == null || userStatus.equals(this.userStatus)) {
            return this;
        }
        return new User(
                this.seq,
                this.auth,
                this.userRole,
                this.reportData,
                this.profile,
                this.location,
                userStatus,
                LocalDateTime.now()
        );
    }

    public User updateNickname(String nickname) {
        if (nickname == null || nickname.trim().isEmpty() || nickname.equals(this.profile.nickname())) {
            return this;
        }
        Profile newProfile = Profile.of(nickname, this.profile.imageUrl(), this.profile.interestCountry());
        return new User(
                this.seq,
                this.auth,
                this.userRole,
                this.reportData,
                newProfile,
                this.location,
                this.userStatus,
                LocalDateTime.now()
        );
    }

    public User updateInterestCountry(Country interestCountry) {
        if (interestCountry == null || interestCountry.equals(this.profile.interestCountry())) {
            return this;
        }
        Profile newProfile = Profile.of(this.profile.nickname(), this.profile.imageUrl(), interestCountry);
        return new User(
                this.seq,
                this.auth,
                this.userRole,
                this.reportData,
                newProfile,
                this.location,
                this.userStatus,
                LocalDateTime.now()
        );
    }

    public User updateImageUrl(String imageUrl) {
        if (imageUrl.equals(this.profile.imageUrl())) {
            return this;
        }
        Profile newProfile = Profile.of(this.profile.nickname(), imageUrl, this.profile.interestCountry());
        return new User(
                this.seq,
                this.auth,
                this.userRole,
                this.reportData,
                newProfile,
                this.location,
                this.userStatus,
                LocalDateTime.now()
        );
    }

    public User updateRegion(String region) {
        if (region == null || region.trim().isEmpty() || region.equals(this.location.region())) {
            return this;
        }
        return new User(
                this.seq,
                this.auth,
                this.userRole,
                this.reportData,
                this.profile,
                Location.of(this.location.country(), region),
                this.userStatus,
                LocalDateTime.now()
        );
    }

    public User updateCountry(Country country) {
        if (country == null || country.equals(this.location.country())) {
            return this;
        }
        Location newLocation = Location.of(country, this.location.region());
        return new User(
                this.seq,
                this.auth,
                this.userRole,
                this.reportData,
                this.profile,
                newLocation,
                this.userStatus,
                LocalDateTime.now()
        );
    }


    public User increaseReportedCount() {
        Long newCount = this.reportData.reportedCount() + 1;
        ReportData newReport = ReportData.of(newCount, Date.valueOf(LocalDate.now()));
        return new User(
                this.seq,
                this.auth,
                this.userRole,
                newReport,
                this.profile,
                this.location,
                this.userStatus,
                this.updatedAt
        );
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

}
