package com.backend.immilog.user.application.command;

public record UserSignInCommand(
        String email,
        String password,
        Double latitude,
        Double longitude
) {
}
