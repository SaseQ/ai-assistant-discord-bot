package it.marczuk.aiassistantdiscordbot.web.gpt.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GPTBody {

    @JsonProperty("model")
    private final String gptVersion;
    @JsonProperty("messages")
    private final GPTMessage[] prompts;

    public GPTBody(GPTVersion gptVersion, GPTMessage[] prompts) {
        this.gptVersion = gptVersion.getCode();
        this.prompts = prompts;
    }
}
