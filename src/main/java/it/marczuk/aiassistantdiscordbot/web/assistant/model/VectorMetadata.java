package it.marczuk.aiassistantdiscordbot.web.assistant.model;

import lombok.Data;

@Data
public class VectorMetadata {

    private final String id;
    private final String tags;
    private final String group;
}
