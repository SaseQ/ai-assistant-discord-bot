package it.marczuk.aiassistantdiscordbot.web.assistant.model;

import lombok.Data;

import java.util.List;

@Data
public class Vector {

    private final String id;
    private final List<Double> values;
    private final VectorMetadata metadata;
}
