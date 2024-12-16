package com.backend.immilog.notification.domain.model;

import com.backend.immilog.notification.applicaiton.command.DiscordCommand;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class Discord {
    private String content;
    private List<Embed> embeds;

    @Builder
    public Discord(
            String content,
            List<Embed> embeds
    ) {
        this.content = content;
        this.embeds = embeds;
    }

    public static Discord from(
            String api,
            Embed embed
    ) {
        return Discord.builder()
                .content("[EXTERNAL API] {" + api + "}")
                .embeds(List.of(embed))
                .build();
    }

    public DiscordCommand toRequest() {
        return new DiscordCommand(
                this.content,
                this.embeds.stream().map(Embed::toRequest).toList()
        );
    }

    @Getter
    @NoArgsConstructor
    public static class Embed {
        private String title;
        private List<Field> fields;

        @Builder
        Embed(
                String title,
                List<Field> fields
        ) {
            this.title = title;
            this.fields = fields;
        }

        public static Embed createWithField(Field field) {
            return builder()
                    .title("Exception Detail")
                    .fields(List.of(field))
                    .build();
        }

        public DiscordCommand.Embed toRequest() {
            return new DiscordCommand.Embed(
                    this.title,
                    this.fields.stream().map(Field::toRequest).toList()
            );
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Field {
        private String name;
        private String value;
        private boolean inline;

        @Builder
        Field(
                String name,
                String value,
                boolean inline
        ) {
            this.name = name;
            this.value = value;
            this.inline = inline;
        }

        public static Discord.Field from(Exception exception) {
            return Discord.Field.builder()
                    .name(exception.getClass().getName())
                    .value(exception.getMessage())
                    .inline(true)
                    .build();
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