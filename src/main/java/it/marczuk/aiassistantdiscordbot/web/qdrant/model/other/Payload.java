package it.marczuk.aiassistantdiscordbot.web.qdrant.model.other;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Payload {

    private final int id;
    private final String group;
    private final String tags;
}
