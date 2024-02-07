package it.marczuk.aiassistantdiscordbot.web.qdrant.model;

import it.marczuk.aiassistantdiscordbot.web.qdrant.model.other.Point;
import lombok.Data;

import java.util.List;

@Data
public class CollectionObject {

    private final List<Point> points;
}
