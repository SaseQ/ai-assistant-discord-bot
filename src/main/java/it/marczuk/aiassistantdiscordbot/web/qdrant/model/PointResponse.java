package it.marczuk.aiassistantdiscordbot.web.qdrant.model;

import it.marczuk.aiassistantdiscordbot.web.qdrant.model.other.Result;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor(force = true)
public class PointResponse {

    private final int time;
    private final String status;
    private final List<Result> result;
}
