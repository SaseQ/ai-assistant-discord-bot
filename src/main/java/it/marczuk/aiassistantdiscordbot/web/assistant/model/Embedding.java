package it.marczuk.aiassistantdiscordbot.web.assistant.model;

import it.marczuk.aiassistantdiscordbot.web.gpt.model.GPTVersion;
import lombok.Data;

@Data
public class Embedding {

    private final String input;
    private final String model;

    public Embedding(String input, GPTVersion model) {
        this.input = input;
        this.model = model.getCode();
    }
}
