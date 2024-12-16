package com.backend.immilog.notification.applicaiton.service;

import com.backend.immilog.notification.domain.model.Discord;
import com.backend.immilog.notification.infrastructure.gateway.discord.DiscordGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiscordSendingService {
    private final DiscordGateway discordGateway;

    public void send(
            String api,
            Exception exception
    ) {
        Discord.Field field = Discord.Field.from(exception);
        Discord.Embed embed = Discord.Embed.createWithField(field);
        Discord discord = Discord.from(api, embed);
        discordGateway.send(discord.toRequest());
    }
}
