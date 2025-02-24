package com.backend.immilog.notification.domain.model

import com.backend.immilog.notification.application.command.DiscordCommand

data class Discord(
    val content: String,
    val embeds: List<Embed>
) {
    companion object {
        fun from(api: String, embed: Embed): Discord =
            Discord("[EXTERNAL API] {$api}", listOf(embed))
    }

    fun toRequest(): DiscordCommand =
        DiscordCommand(content, embeds.map { it.toRequest() })

    data class Embed(
        val title: String,
        val fields: List<Field>
    ) {
        companion object {
            fun createWith(field: Field): Embed =
                Embed("Exception Detail", listOf(field))
        }

        fun toRequest(): DiscordCommand.Embed =
            DiscordCommand.Embed(title, fields.map { it.toRequest() })
    }

    data class Field(
        val name: String,
        val value: String,
        val inline: Boolean
    ) {
        companion object {
            fun from(exception: Exception): Field =
                Field(exception.javaClass.name, exception.message ?: "", true)
        }

        fun toRequest(): DiscordCommand.Embed.Field =
            DiscordCommand.Embed.Field(name, value, inline)
    }
}
