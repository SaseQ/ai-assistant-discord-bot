package it.marczuk.aiassistantdiscordbot.web.qdrant.model.other;

import lombok.Data;

import java.util.List;

@Data
public class Point {

    private final int id;
    private final Payload payload;
    private final List<Double> vector;
}
