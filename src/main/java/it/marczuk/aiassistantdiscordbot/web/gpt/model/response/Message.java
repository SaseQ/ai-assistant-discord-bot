package it.marczuk.aiassistantdiscordbot.web.gpt.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(force = true)
public class Message {

    @JsonProperty("role")
    private final String role;
    @JsonProperty("content")
    private final String content;
}
