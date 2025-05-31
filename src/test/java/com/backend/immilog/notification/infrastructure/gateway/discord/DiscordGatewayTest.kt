package com.backend.immilog.notification.infrastructure.gateway.discord

import com.backend.immilog.notification.application.DiscordCommand
import com.backend.immilog.notification.infrastructure.DiscordGateway
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.*
import org.springframework.web.client.RestClient

@DisplayName("DiscordGateway 클래스 테스트")
class DiscordGatewayTest {

    private val restClient: RestClient = mock(RestClient::class.java)
    private val discordProperties: DiscordGateway.DiscordProperties = mock(DiscordGateway.DiscordProperties::class.java)
    private val discordGateway = DiscordGateway(restClient, discordProperties)

    @BeforeEach
    fun setUp() {
        `when`(discordProperties.webhookUrl).thenReturn("http://test-webhook-url")
    }

    @Test
    @DisplayName("send 메서드 성공")
    fun sendShouldSendCorrectDiscordCommand() {
        // Given
        val discordCommand = DiscordCommand(
                content = "Test Content",
                embeds = listOf(
                        DiscordCommand.Embed(
                                title = "Test Title",
                                fields = listOf(
                                        DiscordCommand.Embed.Field("Field Name", "Field Value", true)
                                )
                        )
                )
        )

        val bodyCaptor: ArgumentCaptor<String> = ArgumentCaptor.forClass(String::class.java)
        val requestSpec: RestClient.RequestBodyUriSpec = mock(RestClient.RequestBodyUriSpec::class.java)
        `when`(restClient.post()).thenReturn(requestSpec)
        `when`(requestSpec.uri("http://test-webhook-url")).thenReturn(requestSpec)
        `when`(requestSpec.headers(any())).thenReturn(requestSpec)
        `when`(requestSpec.body(anyString())).thenReturn(requestSpec)

        // When
        discordGateway.send(discordCommand)

        // Then
        verify(requestSpec).body(bodyCaptor.capture())
        val capturedBody = bodyCaptor.value

        val expectedJson = """
            {
              "content": "Test Content",
              "embeds": [
                {
                  "title": "Test Title",
                  "fields": [
                    {
                      "name": "Field Name",
                      "value": "Field Value",
                      "inline": true
                    }
                  ]
                }
              ]
            }
        """.trimIndent()

        assertThat(capturedBody).isEqualToIgnoringWhitespace(expectedJson)
    }
}
