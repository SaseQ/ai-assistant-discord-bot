package it.marczuk.aiassistantdiscordbot.web.nocodb.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResourceRequest {

    private final String title;
    private final String description;
    private final String url;
    private final String tags;
    private final String category;
}
