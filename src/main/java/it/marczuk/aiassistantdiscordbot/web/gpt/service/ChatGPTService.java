package it.marczuk.aiassistantdiscordbot.web.gpt.service;

import it.marczuk.aiassistantdiscordbot.web.gpt.configuration.ChatGPTConfig;
import it.marczuk.aiassistantdiscordbot.web.gpt.exception.BadRequestToRestTemplateException;
import it.marczuk.aiassistantdiscordbot.web.gpt.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ChatGPTService {

    public static final String OPEN_AI_URL = "https://api.openai.com/v1/";

    private final ChatGPTConfig chatGPTConfig;
    private final RestTemplate restTemplate;

    public GPTResponse interactWithChatGPT(GPTMessage[] messages) {
        String apiKey = chatGPTConfig.getApiKey();
        GPTBody gptBody = new GPTBody(GPTVersion.GPT_3_5, messages);
        GPTResponse response = callGetMethod("chat/completions", HttpMethod.POST, getHttpEntity(gptBody, apiKey), GPTResponse.class);
        return response;
    }

    private <T> T callGetMethod(String url, HttpMethod method,
                                HttpEntity<?> requestEntity, Class<T> responseType, Object... objects) {
        try {
            ResponseEntity<T> response = restTemplate.exchange(OPEN_AI_URL + url, method, requestEntity, responseType, objects);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new BadRequestToRestTemplateException(OPEN_AI_URL + url + " | " + e.getMessage(), e.getStatusCode().toString());
        }
    }

    private HttpEntity<Object> getHttpEntity(GPTBody gptBody, String apiKey) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + apiKey);
        return new HttpEntity<>(gptBody, headers);
    }
}
