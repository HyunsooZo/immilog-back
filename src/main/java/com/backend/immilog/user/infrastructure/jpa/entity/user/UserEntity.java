package com.backend.immilog.user.infrastructure.jpa.entity.user;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.global.enums.UserRole;
import com.backend.immilog.user.domain.enums.UserStatus;
import com.backend.immilog.user.domain.model.user.User;
import com.backend.immilog.user.exception.UserErrorCode;
import com.backend.immilog.user.exception.UserException;
import jakarta.persistence.*;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.Date;
import java.time.LocalDateTime;

@DynamicUpdate
@Entity
@Table(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;

    @Embedded
    private AuthValue auth;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status")
    private UserStatus userStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private UserRole userRole;

    @Embedded
    private LocationValue location;

    @Embedded
    private ReportValue report;

    @Embedded
    private ProfileValue profile;

    @Column(name = "created_at")
    private final LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected UserEntity() {}

    protected UserEntity(
            Long seq,
            String email,
            String password,
            UserStatus userStatus,
            UserRole userRole,
            Long reportedCount,
            Date reportedDate,
            String nickname,
            String imageUrl,
            Country country,
            String region,
            Country interestCountry,
            LocalDateTime updatedAt
    ) {
        AuthValue auth = AuthValue.of(email, password);
        ProfileValue profile = ProfileValue.of(nickname, imageUrl, interestCountry);
        LocationValue location = LocationValue.of(country, region);
        ReportValue reportData = ReportValue.of(reportedCount, reportedDate);
        this.seq = seq;
        this.auth = auth;
        this.userStatus = userStatus;
        this.userRole = userRole;
        this.profile = profile;
        this.location = location;
        this.report = reportData;
        this.updatedAt = updatedAt;
    }

    public static UserEntity from(User user) {
        return new UserEntity(
                user.seq(),
                user.auth().email(),
                user.auth().password(),
                user.userStatus(),
                user.userRole(),
                user.reportedCount(),
                user.reportedDate(),
                user.nickname(),
                user.imageUrl(),
                user.country(),
                user.region(),
                user.interestCountry(),
                user.updatedAt()
        );
    }

    public User toDomain() {
        if (this.seq == null || this.userRole == null || this.userStatus == null) {
            throw new UserException(UserErrorCode.ENTITY_TO_DOMAIN_ERROR);
        }
        return new User(
                this.seq,
                this.auth.toDomain(),
                this.userRole,
                this.report == null ? null : this.report.toDomain(),
                this.profile.toDomain(),
                this.location.toDomain(),
                this.userStatus,
                this.updatedAt
        );
    }
}

