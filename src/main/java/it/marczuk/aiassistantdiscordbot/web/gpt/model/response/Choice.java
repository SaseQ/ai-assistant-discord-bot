package it.marczuk.aiassistantdiscordbot.web.gpt.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(force = true)
public class Choice {

    @JsonProperty("index")
    private final int index;
    @JsonProperty("message")
    private final Message message;
    @JsonProperty("finish_reason")
    private final String finishReason;
}
