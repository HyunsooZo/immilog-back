package com.backend.immilog.notification.applicaiton.service;

import com.backend.immilog.notification.applicaiton.command.DiscordCommand;
import com.backend.immilog.notification.infrastructure.gateway.discord.DiscordGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@DisplayName("DiscordSendingService 클래스")
class DiscordSendingServiceTest {

    private final DiscordGateway discordGateway = mock(DiscordGateway.class);
    private final DiscordSendingService discordSendingService = new DiscordSendingService(discordGateway);

    @Test
    @DisplayName("디스코드 메시지 전송 성공")
    void sendMethodSendsCorrectDiscordRequest() {
        String api = "testApi";
        Exception exception = new Exception("testException");
        discordSendingService.send(api, exception);
        ArgumentCaptor<DiscordCommand> captor = ArgumentCaptor.forClass(DiscordCommand.class);
        verify(discordGateway).send(captor.capture());
        DiscordCommand capturedRequest = captor.getValue();
        assertThat(capturedRequest.content()).isEqualTo("[EXTERNAL API] {testApi}");
        assertThat(capturedRequest.embeds().getFirst().fields().getFirst().value()).isEqualTo("testException");
    }
}