package it.marczuk.aiassistantdiscordbot.bot.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;

@Component
public class Ping extends SlashCommand {

    public Ping() {
        this.name = "ping";
        this.help = "Display lag between bot and discord";
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        event.reply("Ping: ...").queue(m -> {
            final long ping = event.getTimeCreated().until(m.getInteraction().getTimeCreated(), ChronoUnit.MILLIS);
            m.editOriginal("Pong! "+ ping  + "ms | Websocket: " + event.getJDA().getGatewayPing() + "ms").queue();
        });
    }
}
