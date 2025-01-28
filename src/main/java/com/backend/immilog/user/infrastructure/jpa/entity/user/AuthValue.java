package com.backend.immilog.user.infrastructure.jpa.entity.user;

import com.backend.immilog.user.domain.model.user.Auth;
import com.backend.immilog.user.exception.UserErrorCode;
import com.backend.immilog.user.exception.UserException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
@Embeddable
public class AuthValue {
    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    protected AuthValue() {}

    protected AuthValue(
            String email,
            String password
    ) {
        this.email = email;
        this.password = password;
    }

    public static AuthValue of(
            String email,
            String password
    ) {
        return new AuthValue(email, password);
    }

    public Auth toDomain() {
        if(this.email == null || this.password == null) {
            throw new UserException(UserErrorCode.ENTITY_TO_DOMAIN_ERROR);
        }
        return Auth.of(this.email, this.password);
    }

}
