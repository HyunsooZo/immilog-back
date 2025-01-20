package com.backend.immilog.user.infrastructure.jpa.entity;

import com.backend.immilog.global.enums.UserRole;
import com.backend.immilog.user.domain.enums.UserCountry;
import com.backend.immilog.user.domain.enums.UserStatus;
import com.backend.immilog.user.domain.model.user.Location;
import com.backend.immilog.user.domain.model.user.ReportInfo;
import com.backend.immilog.user.domain.model.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@DynamicUpdate
@Entity
@Table(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "image_url")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status")
    private UserStatus userStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private UserRole userRole;

    @Enumerated(EnumType.STRING)
    @Column(name = "interest_country")
    private UserCountry interestCountry;

    @Embedded
    private Location location;

    @Embedded
    private ReportInfo reportInfo;

    @Column(name = "created_at")
    private final LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected UserEntity() {}

    @Builder
    protected UserEntity(
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
        this.updatedAt = updatedAt;
    }

    public static UserEntity from(User user) {
        return UserEntity.builder()
                .seq(user.getSeq())
                .nickName(user.getNickname())
                .email(user.getEmail())
                .password(user.getPassword())
                .imageUrl(user.getImageUrl())
                .userStatus(user.getUserStatus())
                .userRole(user.getUserRole())
                .interestCountry(user.getInterestCountry())
                .location(user.getLocation())
                .reportInfo(user.getReportInfo())
                .updatedAt(user.getSeq() != null ? LocalDateTime.now() : null)
                .build();
    }

    public User toDomain() {
        return User.builder()
                .seq(this.seq)
                .nickName(this.nickname)
                .email(this.email)
                .password(this.password)
                .imageUrl(this.imageUrl)
                .userStatus(this.userStatus)
                .userRole(this.userRole)
                .interestCountry(this.interestCountry)
                .location(this.location)
                .reportInfo(this.reportInfo)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}

