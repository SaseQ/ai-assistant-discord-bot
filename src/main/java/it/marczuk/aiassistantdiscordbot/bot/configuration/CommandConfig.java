package it.marczuk.aiassistantdiscordbot.bot.configuration;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import io.github.cdimascio.dotenv.Dotenv;
import it.marczuk.aiassistantdiscordbot.bot.Bot;
import it.marczuk.aiassistantdiscordbot.bot.command.AskGPT;
import it.marczuk.aiassistantdiscordbot.bot.command.Interact;
import it.marczuk.aiassistantdiscordbot.bot.command.Ping;
import it.marczuk.aiassistantdiscordbot.bot.event.PrivateMessageEvent;
import it.marczuk.aiassistantdiscordbot.bot.exception.JdaException;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.security.auth.login.LoginException;

@Configuration
@Slf4j
public class CommandConfig {

    private JDA jda;
    private final Dotenv env;

    private final Ping ping;
    private final AskGPT askGPT;
    private final Interact interact;

    private final PrivateMessageEvent privateMessageEvent;

    @Autowired
    public CommandConfig(Ping ping, AskGPT askGPT, Interact interact, PrivateMessageEvent privateMessageEvent) throws LoginException {
        env = Bot.getInstance().getEnv();

        this.ping = ping;
        this.askGPT = askGPT;
        this.interact = interact;

        this.privateMessageEvent = privateMessageEvent;
    }

    @PostConstruct
    public void init() {
        try {
            jda = Bot.getInstance().getJda();
            jda.addEventListener(getBotConfig());
        } catch (LoginException e) {
            throw new JdaException("Jda not exists");
        }
    }

    private CommandClient getBotConfig() {
        CommandClientBuilder builder = new CommandClientBuilder();
        builder.setOwnerId(env.get("OWNER_ID"));
        builder.forceGuildOnly(env.get("FORCE_GUILD"));
        builder.setActivity(Activity.watching(env.get("ACTIVITY")));

        // Registration SlashCommands
        builder.addSlashCommand(ping);
        builder.addSlashCommand(askGPT);
        builder.addSlashCommand(interact);

        // Registration ListenerAdapters
        jda.addEventListener(privateMessageEvent);

        log.info("Jda configuration loaded!");
        return builder.build();
    }
}
