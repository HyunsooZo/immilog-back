package com.backend.immilog.notification.applicaiton.command;

import java.util.List;

public record DiscordCommand(
        String content,
        List<Embed> embeds
) {
    public record Embed(
            String title,
            List<Field> fields
    ) {
    }

    public record Field(
            String name,
            String value,
            boolean inline
    ) {
    }
}
