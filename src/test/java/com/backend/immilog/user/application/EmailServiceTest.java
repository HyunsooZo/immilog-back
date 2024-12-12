package com.backend.immilog.user.application;

import com.backend.immilog.user.application.services.EmailService;
import com.backend.immilog.user.exception.UserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.backend.immilog.post.exception.PostErrorCode.EMAIL_SEND_FAILED;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("이메일 서비스 테스트")
class EmailServiceTest {
    private final JavaMailSender javaMailSender = mock(JavaMailSender.class);
    private final EmailService emailService = new EmailService(javaMailSender);

    @Test
    @DisplayName("이메일 발송 - 성공")
    void sendHtmlEmail_success() {
        // given
        String to = "to@example.com";
        String subject = "subject";
        String htmlBody = "htmlBody";
        MimeMessage message = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(message);

        // when
        emailService.sendHtmlEmail(to, subject, htmlBody);

        // then
        verify(javaMailSender, times(1)).send(message);
    }

    @Test
    @DisplayName("이메일 발송 실패 - MimeMessageHelper에서 MessagingException 발생")
    void sendHtmlEmail_fail_dueToMimeMessageHelperException() throws MessagingException {
        // given
        String to = "to@example.com";
        String subject = "subject";
        String htmlBody = "htmlBody";
        MimeMessage message = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(message);
        doThrow(new MessagingException()).when(message).setContent(any());
        // when & then
        assertThatThrownBy(() -> {
            emailService.sendHtmlEmail(to, subject, htmlBody);
        })
                .isInstanceOf(UserException.class)
                .hasMessage(EMAIL_SEND_FAILED.getMessage());
    }

    @Test
    @DisplayName("이메일 발송 - 비동기 처리")
    void sendHtmlEmail_async() throws ExecutionException, InterruptedException {
        // given
        String to = "to@example.com";
        String subject = "subject";
        String htmlBody = "htmlBody";
        MimeMessage message = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(message);

        // when
        CompletableFuture<Void> future = CompletableFuture.runAsync(() ->
                emailService.sendHtmlEmail(to, subject, htmlBody)
        );

        // then
        future.get();
        verify(javaMailSender, times(1)).send(message);
    }
}
