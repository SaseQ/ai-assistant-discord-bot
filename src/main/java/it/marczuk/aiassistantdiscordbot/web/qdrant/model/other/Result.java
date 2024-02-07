package it.marczuk.aiassistantdiscordbot.web.qdrant.model.other;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor(force = true)
public class Result {

    private final int id;
    private final Payload payload;
    private final Double score;
    private final List<Double> vector;
    @JsonProperty("shard_key")
    private final String shardKey;
}
