package it.marczuk.aiassistantdiscordbot.bot.event;

import io.github.cdimascio.dotenv.Dotenv;
import it.marczuk.aiassistantdiscordbot.bot.Bot;
import it.marczuk.aiassistantdiscordbot.web.assistant.service.AssistantService;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import javax.security.auth.login.LoginException;

@Component
@Slf4j
public class PrivateMessageEvent extends ListenerAdapter {

    private final AssistantService assistantService;
    private final Dotenv env;

    public PrivateMessageEvent(AssistantService assistantService) throws LoginException {
        this.assistantService = assistantService;
        this.env = Bot.getInstance().getEnv();
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        User author = event.getMessage().getAuthor();
        MessageChannelUnion channel = event.getChannel();
        if(!channel.getId().equals(env.get("PRIVATE_CHANNEL_ID"))) {
            return;
        }
        if(!author.getId().equals(env.get("OWNER_ID"))) {
            return;
        }
        Message message = event.getMessage();
        String contentRaw = message.getContentRaw();
        if(contentRaw.isEmpty()) {
            return;
        }
        message.reply("<a:load:1054839929433227365>").queue(m ->
                m.editMessage(assistantService.interactWithAssistant(contentRaw)).queue()
        );
    }
}
