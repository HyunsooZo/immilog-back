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
    private String nickName;
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
        this.nickName = nickName;
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
        UserCountry interestCountry = command.interestCountry() != null
                ? UserCountry.valueOf(command.interestCountry())
                : null;
        UserCountry country = UserCountry.valueOf(command.country());

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
        Optional.ofNullable(encodedPassword).ifPresent(password -> {
            this.password = password;
            updateTimestamp();
        });
    }

    public void changeUserStatus(UserStatus userStatus) {
        Optional.ofNullable(userStatus).ifPresent(status -> {
            this.userStatus = status;
            updateTimestamp();
        });
    }

    public void changeNickName(String nickName) {
        Optional.ofNullable(nickName).ifPresent(name -> {
            this.nickName = name;
            updateTimestamp();
        });
    }

    public void changeInterestCountry(UserCountry interestCountry) {
        Optional.ofNullable(interestCountry).ifPresent(country -> {
            this.interestCountry = country;
            updateTimestamp();
        });
    }

    public void changeImageUrl(String imageUrl) {
        Optional.ofNullable(imageUrl).ifPresent(url -> {
            this.imageUrl = url;
            updateTimestamp();
        });
    }

    public void changeRegion(String second) {
        Optional.ofNullable(second).ifPresent(region -> {
            this.location = Location.of(this.location.getCountry(), region);
            updateTimestamp();
        });
    }

    public void changeCountry(UserCountry country) {
        Optional.ofNullable(country).ifPresent(value -> {
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
