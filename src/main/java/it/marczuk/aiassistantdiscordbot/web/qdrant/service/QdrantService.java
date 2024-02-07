package it.marczuk.aiassistantdiscordbot.web.qdrant.service;

import it.marczuk.aiassistantdiscordbot.web.qdrant.model.*;
import it.marczuk.aiassistantdiscordbot.web.qdrant.model.other.Point;
import it.marczuk.aiassistantdiscordbot.web.qdrant.model.other.Vectors;
import it.marczuk.aiassistantdiscordbot.web.rest.exception.BadRequestToRestTemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QdrantService {

    private static final String QDRANT_URL = "http://localhost:6333";

    private final RestTemplate restTemplate;

    public Map<Object, Object> deletePoint(String name, PointDelete pointDelete) {
        return callGetMethod("/collections/" + name + "/points/delete", HttpMethod.POST, getHttpEntity(pointDelete), Map.class);
    }

    public PointResponse getPoint(String name, CollectionSearch collectionSearch) {
        return callGetMethod("/collections/" + name + "/points/search", HttpMethod.POST, getHttpEntity(collectionSearch), PointResponse.class);
    }

    public Map<Object, Object> addData(String name, List<Point> points) {
        CollectionObject collectionObject = new CollectionObject(points);
        return callGetMethod("/collections/" + name + "/points?wait=true", HttpMethod.PUT, getHttpEntity(collectionObject), Map.class);
    }

    public Map<Object, Object> crateCollection(String name) {
        CollectionStructure structure = new CollectionStructure(
                new Vectors(
                        1536,
                        "Cosine"
                )
        );
        return callGetMethod("/collections/" + name, HttpMethod.PUT, getHttpEntity(structure), Map.class);
    }

    private <T> T callGetMethod(String url, HttpMethod method,
                                HttpEntity<?> requestEntity, Class<T> responseType, Object... objects) {
        try {
            ResponseEntity<T> response = restTemplate.exchange(QDRANT_URL + url, method, requestEntity, responseType, objects);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new BadRequestToRestTemplateException(QDRANT_URL + url + " | " + e.getMessage(), e.getStatusCode().toString());
        }
    }

    private HttpEntity<Object> getHttpEntity(Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        return new HttpEntity<>(body, headers);
    }
}
