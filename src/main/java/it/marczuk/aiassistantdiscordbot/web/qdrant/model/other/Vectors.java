package it.marczuk.aiassistantdiscordbot.web.qdrant.model.other;

import lombok.Data;

@Data
public class Vectors {

    private final int size;
    private final String distance;
}
