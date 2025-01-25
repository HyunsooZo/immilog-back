package com.backend.immilog.user.application.command;

public record UserPasswordChangeCommand(
        String existingPassword,
        String newPassword
) {
}