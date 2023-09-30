package it.marczuk.aiassistantdiscordbot.bot.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Test extends SlashCommand {

    public Test() {
        this.name = "test";
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        // Test Content
    }
}
