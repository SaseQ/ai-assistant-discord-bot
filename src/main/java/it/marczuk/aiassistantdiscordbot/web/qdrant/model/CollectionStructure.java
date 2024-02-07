package it.marczuk.aiassistantdiscordbot.web.qdrant.model;

import it.marczuk.aiassistantdiscordbot.web.qdrant.model.other.Vectors;
import lombok.Data;

@Data
public class CollectionStructure {

    private final Vectors vectors;
}
