package it.marczuk.aiassistantdiscordbot.bot.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import it.marczuk.aiassistantdiscordbot.web.assistant.service.AssistantService;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class Interact extends SlashCommand {

   private final AssistantService assistantService;

    public Interact(AssistantService assistantService) {
        this.name = "interact";
        this.options = List.of(
                new OptionData(
                        OptionType.STRING,
                        "query",
                        "Type your query...",
                        true
                ).setMaxLength(2500)
        );

        this.assistantService = assistantService;
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        final OptionMapping query = event.getOption("query");

        if (query == null) {
            event.reply(":warning: You need to type the option")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        event.reply("<a:load:1054839929433227365>").queue(m ->
                    m.editOriginal(
                            assistantService.interactWithAssistant(query.getAsString())
                    ).queue()
        );
    }
}
