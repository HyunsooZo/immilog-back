package com.backend.immilog.notification.application.service

import com.backend.immilog.notification.domain.model.Discord
import com.backend.immilog.notification.infrastructure.gateway.discord.DiscordGateway
import org.springframework.stereotype.Service

@Service
class DiscordSendingService(
    private val discordGateway: DiscordGateway
) {
    fun send(api: String, exception: Exception) {
        val field = Discord.Field.from(exception)
        val embed = Discord.Embed.createWith(field)
        val discord = Discord.from(api, embed)
        discordGateway.send(discord.toRequest())
    }
}
