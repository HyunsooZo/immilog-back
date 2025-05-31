package com.backend.immilog.user.application.usecase;

import com.backend.immilog.user.exception.UserErrorCode;
import com.backend.immilog.user.exception.UserException;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

public interface EmailSendUseCase {
    void sendHtmlEmail(
            String to,
            String subject,
            String htmlBody
    );

    @Slf4j
    @Service
    class EmailSender implements EmailSendUseCase {
        private final JavaMailSender javaMailSender;

        public EmailSender(JavaMailSender javaMailSender) {
            this.javaMailSender = javaMailSender;
        }

        @Async
        @Override
        public void sendHtmlEmail(
                String to,
                String subject,
                String htmlBody
        ) {
            var message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = null;
            try {
                helper = new MimeMessageHelper(message, true, "UTF-8");
                helper.setFrom("your-email@gmail.com");
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setText(htmlBody, true);  // true는 HTML을 의미함
            } catch (MessagingException e) {
                log.error("Failed to send email", e);
                throw new UserException(UserErrorCode.EMAIL_SEND_FAILED);
            }

            javaMailSender.send(message);
        }
    }
}
