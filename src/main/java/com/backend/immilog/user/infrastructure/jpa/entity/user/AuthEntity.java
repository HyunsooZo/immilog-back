package com.backend.immilog.user.infrastructure.jpa.entity.user;

import com.backend.immilog.user.domain.model.user.Auth;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
@Embeddable
public class AuthEntity {
    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    protected AuthEntity() {}

    protected AuthEntity(
            String email,
            String password
    ) {
        this.email = email;
        this.password = password;
    }

    public static AuthEntity of(
            String email,
            String password
    ) {
        return new AuthEntity(email, password);
    }

    public Auth toDomain() {
        return Auth.of(this.email, this.password);
    }

}
