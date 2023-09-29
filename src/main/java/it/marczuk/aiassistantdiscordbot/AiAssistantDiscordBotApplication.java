package it.marczuk.aiassistantdiscordbot;

import it.marczuk.aiassistantdiscordbot.bot.Bot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.security.auth.login.LoginException;

@SpringBootApplication
public class AiAssistantDiscordBotApplication {

    public static void main(String[] args) throws LoginException {
        SpringApplication.run(AiAssistantDiscordBotApplication.class, args);
        Bot.getInstance();
    }

}
