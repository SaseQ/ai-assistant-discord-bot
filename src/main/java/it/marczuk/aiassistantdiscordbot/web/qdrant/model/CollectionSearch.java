package it.marczuk.aiassistantdiscordbot.web.qdrant.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CollectionSearch {

    private final List<Double> vector;
    @JsonProperty("with_vectors")
    private final boolean withVectors;
    @JsonProperty("with_payload")
    private final boolean withPayload;
    private final int limit;
}
