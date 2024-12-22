package com.backend.immilog.notification.infrastructure.gateway.discord;

import com.backend.immilog.notification.applicaiton.command.DiscordCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.client.RestClient;

import java.lang.reflect.Field;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("DiscordGateway 클래스 테스트")
class DiscordGatewayTest {

    private final RestClient restClient = mock(RestClient.class);
    private final DiscordGateway discordGateway = new DiscordGateway(restClient);

    @BeforeEach
    void setUp() throws Exception {
        Field webHookUrlField = DiscordGateway.class.getDeclaredField("webHookUrl");
        webHookUrlField.setAccessible(true);
        webHookUrlField.set(discordGateway, "http://test-webhook-url");
    }

    @Test
    @DisplayName("send 메서드 성공")
    void sendShouldSendCorrectDiscordCommand() {
        // Given
        DiscordCommand discordCommand = new DiscordCommand(
                "Test Content",
                List.of(new DiscordCommand.Embed("Test Title", List.of(
                        new DiscordCommand.Field("Field Name", "Field Value", true)
                )))
        );

        ArgumentCaptor<String> bodyCaptor = ArgumentCaptor.forClass(String.class);
        RestClient.RequestBodyUriSpec requestSpec = mock(RestClient.RequestBodyUriSpec.class);
        when(restClient.post()).thenReturn(requestSpec);
        when(requestSpec.uri("http://test-webhook-url")).thenReturn(requestSpec);
        when(requestSpec.headers(any())).thenReturn(requestSpec);
        when(requestSpec.body(anyString())).thenReturn(requestSpec);

        // When
        discordGateway.send(discordCommand);

        // Then
        verify(requestSpec).body(bodyCaptor.capture());
        String capturedBody = bodyCaptor.getValue();

        String expectedJson = """
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
        """;

        assertThat(capturedBody).isEqualToIgnoringWhitespace(expectedJson);
    }

}
