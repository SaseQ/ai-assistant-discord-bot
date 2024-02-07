package it.marczuk.aiassistantdiscordbot.web.nocodb.service;

import it.marczuk.aiassistantdiscordbot.web.nocodb.configuration.NocoDBConfig;
import it.marczuk.aiassistantdiscordbot.web.nocodb.model.ResourceRequest;
import it.marczuk.aiassistantdiscordbot.web.nocodb.model.ResourceResponse;
import it.marczuk.aiassistantdiscordbot.web.rest.exception.BadRequestToRestTemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class NocoDBService {

    private static final String NOCODB_URL = "http://localhost:6335/api/v1/db/data/v1/p3b0o7z1z0ruw8d";

    private final RestTemplate restTemplate;
    private final NocoDBConfig config;

    public ResourceResponse getResourceByRecordId(int recordId) {
        return callGetMethod("/Resources/" + recordId, HttpMethod.GET, getHttpEntity(null), ResourceResponse.class);
    }

    public ResourceResponse addResource(ResourceRequest resourceRequest) {
        return callGetMethod("/Resources", HttpMethod.POST, getHttpEntity(resourceRequest), ResourceResponse.class);
    }

    public ResourceResponse updateResource(int recordId, Map<String, Object> updateMap) {
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        return callGetMethod("/Resources/" + recordId, HttpMethod.PATCH, getHttpEntity(updateMap), ResourceResponse.class);
    }

    public void deleteResource(int recordId) {
        callGetMethod("/Resources/" + recordId, HttpMethod.DELETE, getHttpEntity(null), String.class);
    }

    private <T> T callGetMethod(String url, HttpMethod method,
                                HttpEntity<?> requestEntity, Class<T> responseType, Object... objects) {
        try {
            ResponseEntity<T> response = restTemplate.exchange(NOCODB_URL + url, method, requestEntity, responseType, objects);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new BadRequestToRestTemplateException(NOCODB_URL + url + " | " + e.getMessage(), e.getStatusCode().toString());
        }
    }

    private HttpEntity<Object> getHttpEntity(Object body) {
        String apiKey = config.getApiKey();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("xc-token", apiKey);
        return new HttpEntity<>(body, headers);
    }
}
