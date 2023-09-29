package it.marczuk.aiassistantdiscordbot.web.gpt.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.marczuk.aiassistantdiscordbot.web.gpt.model.response.Choice;
import it.marczuk.aiassistantdiscordbot.web.gpt.model.response.Usage;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor(force = true)
public class GPTResponse {

    @JsonProperty("id")
    private final String id;
    @JsonProperty("object")
    private final String object;
    @JsonProperty("created")
    private final long created;
    @JsonProperty("model")
    private final String model;
    @JsonProperty("choices")
    private final ArrayList<Choice> choices;
    @JsonProperty("usage")
    private final Usage usage;
}

