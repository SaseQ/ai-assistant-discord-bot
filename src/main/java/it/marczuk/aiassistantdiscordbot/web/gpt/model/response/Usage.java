package it.marczuk.aiassistantdiscordbot.web.gpt.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(force = true)
public class Usage {

    @JsonProperty("prompt_tokens")
    private final int promptTokens;
    @JsonProperty("completion_tokens")
    private final int completionTokens;
    @JsonProperty("total_tokens")
    private final int totalTokens;
}
