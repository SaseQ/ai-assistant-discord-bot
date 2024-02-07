package it.marczuk.aiassistantdiscordbot.web.assistant.model;

import lombok.Data;

import java.util.List;

@Data
public class Vectors {

    private final List<Vector> vectors;
}
