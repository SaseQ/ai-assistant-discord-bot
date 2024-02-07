package it.marczuk.aiassistantdiscordbot.web.qdrant.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PointDelete {

    private final List<Integer> points;
}
