package it.marczuk.aiassistantdiscordbot.web.gpt.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GPTMessage {

    @JsonProperty("role")
    private final String gptRole;
    @JsonProperty("content")
    private final String content;

    public GPTMessage(GPTRole gptRole, String content) {
        this.gptRole = gptRole.getName();
        this.content = content;
    }
}
