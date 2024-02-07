package it.marczuk.aiassistantdiscordbot.web.assistant.model;

import lombok.Data;

@Data
public class AssistantResponse {

    private final String message;
    private final int statusCode;
}
