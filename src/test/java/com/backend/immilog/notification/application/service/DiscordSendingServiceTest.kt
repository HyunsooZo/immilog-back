package com.backend.immilog.notification.application.service

import com.backend.immilog.notification.application.command.DiscordCommand
import com.backend.immilog.notification.infrastructure.gateway.discord.DiscordGateway
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

@DisplayName("DiscordSendingService 클래스")
class DiscordSendingServiceTest {

    private val discordGateway: DiscordGateway = mock(DiscordGateway::class.java)
    private val discordSendingService = DiscordSendingService(discordGateway)

    @Test
    @DisplayName("디스코드 메시지 전송 성공")
    fun sendMethodSendsCorrectDiscordRequest() {
        // Given
        val api = "testApi"
        val exception = Exception("testException")

        // When
        discordSendingService.send(api, exception)

        val expectedRequest = DiscordCommand(
            content = "[EXTERNAL API] {testApi}",
            embeds = listOf(
                DiscordCommand.Embed(
                    title = "Exception Detail",
                    fields = listOf(
                        DiscordCommand.Embed.Field("java.lang.Exception", "testException", true)
                    )
                )
            )
        )

        verify(discordGateway).send(expectedRequest)
    }
}
