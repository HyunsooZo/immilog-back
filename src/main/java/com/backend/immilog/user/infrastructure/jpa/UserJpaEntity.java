package com.backend.immilog.user.infrastructure.jpa;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.global.enums.UserRole;
import com.backend.immilog.user.domain.model.user.User;
import com.backend.immilog.user.domain.model.user.UserStatus;
import com.backend.immilog.user.exception.UserErrorCode;
import com.backend.immilog.user.exception.UserException;
import jakarta.persistence.*;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.Date;
import java.time.LocalDateTime;

@DynamicUpdate
@Entity
@Table(name = "user")
public class UserJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;

    @Embedded
    private AuthJpaValue auth;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status")
    private UserStatus userStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private UserRole userRole;

    @Embedded
    private LocationJpaValue location;

    @Embedded
    private ReportJpaValue report;

    @Embedded
    private ProfileJpaValue profile;

    @Column(name = "created_at")
    private final LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected UserJpaEntity() {}

    protected UserJpaEntity(
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
        AuthJpaValue auth = AuthJpaValue.of(email, password);
        ProfileJpaValue profile = ProfileJpaValue.of(nickname, imageUrl, interestCountry);
        LocationJpaValue location = LocationJpaValue.of(country, region);
        ReportJpaValue reportData = ReportJpaValue.of(reportedCount, reportedDate);
        this.seq = seq;
        this.auth = auth;
        this.userStatus = userStatus;
        this.userRole = userRole;
        this.profile = profile;
        this.location = location;
        this.report = reportData;
        this.updatedAt = updatedAt;
    }

    public static UserJpaEntity from(User user) {
        return new UserJpaEntity(
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

