package com.backend.immilog.user.infrastructure.jpa.entity.user;

import com.backend.immilog.global.enums.UserRole;
import com.backend.immilog.user.domain.enums.UserCountry;
import com.backend.immilog.user.domain.enums.UserStatus;
import com.backend.immilog.user.domain.model.user.User;
import jakarta.persistence.*;
import lombok.Builder;
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
    private AuthEntity auth;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status")
    private UserStatus userStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private UserRole userRole;

    @Embedded
    private LocationEntity location;

    @Embedded
    private ReportDataEntity report;

    @Embedded
    private ProfileEntity profile;

    @Column(name = "created_at")
    private final LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected UserEntity() {}

    @Builder
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
            UserCountry country,
            String region,
            UserCountry interestCountry,
            LocalDateTime updatedAt
    ) {
        AuthEntity auth = AuthEntity.of(email, password);
        ProfileEntity profile = ProfileEntity.of(nickname, imageUrl, interestCountry);
        LocationEntity location = LocationEntity.of(country, region);
        ReportDataEntity reportData = ReportDataEntity.of(reportedCount, reportedDate);
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
        return UserEntity.builder()
                .seq(user.getSeq())
                .email(user.getEmail())
                .password(user.getPassword())
                .userStatus(user.getUserStatus())
                .userRole(user.getUserRole())
                .country(user.getCountry())
                .region(user.getRegion())
                .reportedCount(user.getReportedCount())
                .reportedDate(user.getReportedDate())
                .nickname(user.getNickname())
                .imageUrl(user.getImageUrl())
                .interestCountry(user.getInterestCountry())
                .updatedAt(user.getSeq() != null ? LocalDateTime.now() : null)
                .build();
    }

    public User toDomain() {
        return User.builder()
                .seq(this.seq)
                .auth(this.auth.toDomain())
                .userStatus(this.userStatus)
                .userRole(this.userRole)
                .location(this.location.toDomain())
                .reportData(this.report.toDomain())
                .profile(this.profile.toDomain())
                .updatedAt(this.updatedAt)
                .build();
    }
}

