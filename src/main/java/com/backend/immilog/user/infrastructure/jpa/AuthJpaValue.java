package com.backend.immilog.user.infrastructure.jpa;

import com.backend.immilog.user.domain.model.user.Auth;
import com.backend.immilog.user.exception.UserErrorCode;
import com.backend.immilog.user.exception.UserException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
@Embeddable
public class AuthJpaValue {
    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    protected AuthJpaValue() {}

    protected AuthJpaValue(
            String email,
            String password
    ) {
        this.email = email;
        this.password = password;
    }

    public static AuthJpaValue of(
            String email,
            String password
    ) {
        return new AuthJpaValue(email, password);
    }

    public Auth toDomain() {
        if(this.email == null || this.password == null) {
            throw new UserException(UserErrorCode.ENTITY_TO_DOMAIN_ERROR);
        }
        return Auth.of(this.email, this.password);
    }

}
