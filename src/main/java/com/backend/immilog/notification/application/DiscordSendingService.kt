package com.backend.immilog.notification.application

import com.backend.immilog.notification.domain.Discord
import com.backend.immilog.notification.infrastructure.DiscordGateway
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
