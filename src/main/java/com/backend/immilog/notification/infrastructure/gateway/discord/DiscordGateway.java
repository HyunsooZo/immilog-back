package com.backend.immilog.notification.infrastructure.gateway.discord;

import com.backend.immilog.notification.applicaiton.command.DiscordCommand;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class DiscordGateway {
    private final RestClient restClient;

    @Value("${discord.webhookURL}")
    private String webHookUrl;

    public void send(DiscordCommand discordCommand) {
        try {
            hook(discordCommand);
        } catch (Exception e) {
            log.error("Discord 메시지 전송 중 에러 발생: {}", e.getMessage(), e);
        }
    }

    private void hook(DiscordCommand discordCommand) {

        restClient.post()
                .uri(webHookUrl)
                .headers(headers -> headers.setContentType(MediaType.APPLICATION_JSON))
                .body(toJson(discordCommand).toString())
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        (rs, response) -> {
                            log.error(
                                    "메시지 전송 중 오류 발생: 상태 코드 {}, 메시지 {}",
                                    response.getStatusCode(),
                                    response.getStatusText()
                            );
                        })
                .toEntity(String.class);
    }

    private JsonObject toJson(DiscordCommand discordCommand) {
        JsonArrayBuilder embedsArrayBuilder = Json.createArrayBuilder();
        for (DiscordCommand.Embed embed : discordCommand.embeds()) {
            embedsArrayBuilder.add(toJson(embed));
        }

        return Json.createObjectBuilder()
                .add("content", discordCommand.content())
                .add("embeds", embedsArrayBuilder)
                .build();
    }

    private JsonObject toJson(DiscordCommand.Embed embed) {
        JsonArrayBuilder fieldsArrayBuilder = Json.createArrayBuilder();
        for (DiscordCommand.Field field : embed.fields()) {
            fieldsArrayBuilder.add(toJson(field));
        }

        return Json.createObjectBuilder()
                .add("title", embed.title())
                .add("fields", fieldsArrayBuilder)
                .build();
    }

    private JsonObject toJson(DiscordCommand.Field field) {
        return Json.createObjectBuilder()
                .add("name", field.name())
                .add("value", field.value())
                .add("inline", field.inline())
                .build();
    }
}
