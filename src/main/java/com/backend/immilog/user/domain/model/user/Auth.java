package com.backend.immilog.user.domain.model.user;

public record Auth(
        String email,
        String password
) {
    public static Auth of(
            String email,
            String password
    ) {
        return new Auth(email, password);
    }
}