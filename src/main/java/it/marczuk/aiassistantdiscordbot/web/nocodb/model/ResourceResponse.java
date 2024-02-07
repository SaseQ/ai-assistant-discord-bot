package it.marczuk.aiassistantdiscordbot.web.nocodb.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(force = true)
public class ResourceResponse {

    @JsonProperty("Id")
    private final int id;
    private final String title;
    @JsonProperty("CreatedAt")
    private final String createdAt;
    @JsonProperty("UpdatedAt")
    private final String updatedAt;
    private final String description;
    private final String url;
    private final String tags;
    private final String category;
    private final boolean synced;
}
