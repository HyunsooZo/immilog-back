package com.backend.immilog.notification.domain.model;

import com.backend.immilog.notification.applicaiton.command.DiscordCommand;

import java.util.List;

public record Discord(
        String content,
        List<Embed> embeds
) {
    public static Discord from(
            String api,
            Embed embed
    ) {
        return new Discord(
                "[EXTERNAL API] {" + api + "}",
                List.of(embed)
        );
    }

    public DiscordCommand toRequest() {
        return new DiscordCommand(
                this.content,
                this.embeds.stream().map(Embed::toRequest).toList()
        );
    }

    public record Embed(
            String title,
            List<Field> fields
    ) {
        public static Embed createWith(Field field) {
            return new Embed(
                    "Exception Detail",
                    List.of(field)
            );
        }

        public DiscordCommand.Embed toRequest() {
            return new DiscordCommand.Embed(
                    this.title,
                    this.fields.stream().map(Field::toRequest).toList()
            );
        }
    }

    public record Field(
            String name,
            String value,
            boolean inline
    ) {

        public static Discord.Field from(Exception exception) {
            return new Discord.Field(
                    exception.getClass().getName(),
                    exception.getMessage(),
                    true
            );
        }

        public DiscordCommand.Field toRequest() {
            return new DiscordCommand.Field(
                    this.name,
                    this.value,
                    this.inline
            );
        }
    }
}