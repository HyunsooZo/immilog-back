package com.backend.immilog.user.domain.model.user;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
public class Auth {
    private final String email;
    private String password;

    protected Auth(
            String email,
            String password
    ) {
        this.email = email;
        this.password = password;
    }

    public static Auth of(
            String email,
            String password
    ) {
        return new Auth(email, password);
    }

    protected void updatePassword(String password) {
        this.password = password;
    }
}
