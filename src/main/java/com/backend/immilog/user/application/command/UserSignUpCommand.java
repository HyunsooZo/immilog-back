package com.backend.immilog.user.application.command;

public record UserSignUpCommand(
        String nickName,
        String password,
        String email,
        String country,
        String interestCountry,
        String region,
        String profileImage
) {
}
