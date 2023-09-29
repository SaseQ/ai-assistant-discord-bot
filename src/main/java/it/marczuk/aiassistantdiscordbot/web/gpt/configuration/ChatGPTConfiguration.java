package it.marczuk.aiassistantdiscordbot.web.gpt.configuration;

import io.github.cdimascio.dotenv.Dotenv;
import it.marczuk.aiassistantdiscordbot.bot.Bot;
import org.springframework.context.annotation.Configuration;

import javax.security.auth.login.LoginException;

@Configuration
public class ChatGPTConfiguration {

    private final Dotenv env;

    public ChatGPTConfiguration() throws LoginException {
        env = Bot.getInstance().getEnv();
    }

    public String getApiKey() {
        return env.get("OPENAI_API_KEY");
    }
}
