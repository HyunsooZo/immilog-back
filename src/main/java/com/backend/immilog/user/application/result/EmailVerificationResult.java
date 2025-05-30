package com.backend.immilog.user.application.result;

public record EmailVerificationResult(
        String message,
        boolean isLoginAvailable
) {
}
