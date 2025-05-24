package com.backend.immilog.user.application.usecase;

public interface EmailSendUseCase {
    void sendHtmlEmail(
            String to,
            String subject,
            String htmlBody
    );
}
