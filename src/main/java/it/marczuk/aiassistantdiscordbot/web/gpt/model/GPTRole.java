package it.marczuk.aiassistantdiscordbot.web.gpt.model;

public enum GPTRole {

    SYSTEM("system"),
    USER("user"),
    ASSISTANT("assistant");

    private final String name;

    GPTRole(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
