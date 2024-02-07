package it.marczuk.aiassistantdiscordbot.web.nocodb.configuration;

import io.github.cdimascio.dotenv.Dotenv;
import it.marczuk.aiassistantdiscordbot.bot.Bot;
import org.springframework.context.annotation.Configuration;

import javax.security.auth.login.LoginException;

@Configuration
public class NocoDBConfig {

    private final Dotenv env;

    public NocoDBConfig() throws LoginException {
        env = Bot.getInstance().getEnv();
    }

    public String getApiKey() {
        return env.get("NOCODB_API_KEY");
    }
}
