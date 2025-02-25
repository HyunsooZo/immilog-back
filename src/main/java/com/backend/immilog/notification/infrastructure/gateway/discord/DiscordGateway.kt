package com.backend.immilog.notification.infrastructure.gateway.discord

import com.backend.immilog.notification.application.command.DiscordCommand
import jakarta.json.Json
import jakarta.json.JsonArrayBuilder
import jakarta.json.JsonObject
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class DiscordGateway(
    private val restClient: RestClient,
    @Value("\${discord.webhookURL}") private val webHookUrl: String
) {
    private val log = LoggerFactory.getLogger(DiscordGateway::class.java)

    fun send(discordCommand: DiscordCommand) {
        try {
            hook(discordCommand)
        } catch (e: Exception) {
            log.error("Discord 메시지 전송 중 에러 발생: {}", e.message, e)
        }
    }

    private fun hook(discordCommand: DiscordCommand) {
        restClient.post()
            .uri(webHookUrl)
            .headers { headers ->
                headers.contentType = MediaType.APPLICATION_JSON
            }
            .body(toJson(discordCommand).toString())
            .retrieve()
            .onStatus(
            { status: HttpStatusCode -> status.isError },
            { _, response ->
                log.error(
                    "메시지 전송 중 오류 발생: 상태 코드 {}, 메시지 {}",
                    response.statusCode,
                    response.statusText
                )
            }
        )
            .toBodilessEntity()
    }

    private fun toJson(discordCommand: DiscordCommand): JsonObject {
        val embedsArrayBuilder: JsonArrayBuilder = Json.createArrayBuilder()
        discordCommand.embeds.forEach { embed ->
            embedsArrayBuilder.add(toJson(embed))
        }
        return Json.createObjectBuilder()
            .add("content", discordCommand.content)
            .add("embeds", embedsArrayBuilder)
            .build()
    }

    private fun toJson(embed: DiscordCommand.Embed): JsonObject {
        val fieldsArrayBuilder: JsonArrayBuilder = Json.createArrayBuilder()
        embed.fields.forEach { field ->
            fieldsArrayBuilder.add(toJson(field))
        }
        return Json.createObjectBuilder()
            .add("title", embed.title)
            .add("fields", fieldsArrayBuilder)
            .build()
    }

    private fun toJson(field: DiscordCommand.Embed.Field): JsonObject {
        return Json.createObjectBuilder()
            .add("name", field.name)
            .add("value", field.value)
            .add("inline", field.inline)
            .build()
    }
}
