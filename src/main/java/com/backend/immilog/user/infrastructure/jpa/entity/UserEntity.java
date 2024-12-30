package com.backend.immilog.user.infrastructure.jpa.entity;

import com.backend.immilog.global.enums.UserRole;
import com.backend.immilog.global.model.BaseDateEntity;
import com.backend.immilog.user.domain.enums.UserCountry;
import com.backend.immilog.user.domain.enums.UserStatus;
import com.backend.immilog.user.domain.model.user.Location;
import com.backend.immilog.user.domain.model.user.ReportInfo;
import com.backend.immilog.user.domain.model.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import org.hibernate.annotations.DynamicUpdate;

@DynamicUpdate
@Entity
@Table(name = "user")
public class UserEntity extends BaseDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private String nickname;

    private String email;

    private String password;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Enumerated(EnumType.STRING)
    private UserCountry interestCountry;

    @Embedded
    private Location location;

    @Embedded
    private ReportInfo reportInfo;

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
            ReportInfo reportInfo
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
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .build();
    }
}

