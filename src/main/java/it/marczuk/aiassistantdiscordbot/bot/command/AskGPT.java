package it.marczuk.aiassistantdiscordbot.bot.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import it.marczuk.aiassistantdiscordbot.web.gpt.model.GPTMessage;
import it.marczuk.aiassistantdiscordbot.web.gpt.model.GPTResponse;
import it.marczuk.aiassistantdiscordbot.web.gpt.model.GPTRole;
import it.marczuk.aiassistantdiscordbot.web.gpt.service.ChatGPTService;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.stereotype.Component;

import java.util.List;

import static net.dv8tion.jda.api.interactions.commands.Command.Choice;


@Component
public class AskGPT extends SlashCommand {

    private final ChatGPTService chatGPTService;

    public AskGPT(ChatGPTService chatGPTService) {
        this.name = "ask-gpt";
        this.help = "Default command to ask chatGPT";
        this.options = List.of(
                new OptionData(
                        OptionType.STRING,
                        "role",
                        "Select role",
                        true
                ).addChoices(
                        new Choice("system", "SYSTEM"),
                        new Choice("user", "USER"),
                        new Choice("assistant", "ASSISTANT")
                ),
                new OptionData(
                        OptionType.STRING,
                        "content",
                        "Type your content...",
                        true
                ).setMaxLength(2500)
        );

        this.chatGPTService = chatGPTService;
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        final OptionMapping role = event.getOption("role");
        final OptionMapping content = event.getOption("content");

        if (role == null || content == null) {
            event.reply(":warning: You need to type the option")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        GPTMessage[] messages = {new GPTMessage(GPTRole.valueOf(role.getAsString()), content.getAsString())};
        GPTResponse response = chatGPTService.interactWithChatGPT(messages);
        event.reply(response.getChoices().get(0).getMessage().getContent()).queue();
    }
}
