package com.backend.immilog.notification.application

data class DiscordCommand(
    val content: String,
    val embeds: List<Embed>
) {
    data class Embed(
        val title: String,
        val fields: List<Field>
    ) {
        data class Field(
            val name: String,
            val value: String,
            val inline: Boolean
        )
    }
}
