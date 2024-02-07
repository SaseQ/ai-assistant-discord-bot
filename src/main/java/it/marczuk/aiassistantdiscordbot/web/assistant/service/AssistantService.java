package it.marczuk.aiassistantdiscordbot.web.assistant.service;

import com.azure.ai.openai.models.*;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.marczuk.aiassistantdiscordbot.web.assistant.exception.AssistantException;
import it.marczuk.aiassistantdiscordbot.web.assistant.model.Enrich;
import it.marczuk.aiassistantdiscordbot.web.gpt.model.GPTVersion;
import it.marczuk.aiassistantdiscordbot.web.gpt.service.ChatGPTService;
import it.marczuk.aiassistantdiscordbot.web.nocodb.model.ResourceRequest;
import it.marczuk.aiassistantdiscordbot.web.nocodb.model.ResourceResponse;
import it.marczuk.aiassistantdiscordbot.web.nocodb.service.NocoDBService;
import it.marczuk.aiassistantdiscordbot.web.qdrant.model.CollectionSearch;
import it.marczuk.aiassistantdiscordbot.web.qdrant.model.PointDelete;
import it.marczuk.aiassistantdiscordbot.web.qdrant.model.PointResponse;
import it.marczuk.aiassistantdiscordbot.web.qdrant.model.other.Payload;
import it.marczuk.aiassistantdiscordbot.web.qdrant.model.other.Point;
import it.marczuk.aiassistantdiscordbot.web.qdrant.model.other.Result;
import it.marczuk.aiassistantdiscordbot.web.qdrant.service.QdrantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssistantService {

    private final ChatGPTService chatGPTService;
    private final QdrantService qdrantService;
    private final NocoDBService nocoDBService;

    public String interactWithAssistant(String query) {
        JsonObject identity = JsonParser.parseString(identifyQuery(query)).getAsJsonObject();
        String type = identity.get("type").getAsString();
        String tools = identity.get("tools").getAsString();
        log.info("type: " + type);
        log.info("tools: " + tools);

        if(type.equals("memories") && tools.contains("brain")) {
            save(query, "memories");
            return "I remembered that: ```" + query + "```";
        }
        if(type.equals("forget") && tools.contains("brain")) {
            List<Integer> recordIdList = getMemories(query);
            if(recordIdList.isEmpty()) {
                return "I know nothing about it :face_with_raised_eyebrow:";
            }
            recordIdList.forEach(this::forget);
            return "I forgot, they were " + recordIdList.size() + " memories.";
        }
        if(type.equals("query")) {
            List<Integer> recordIdList = getMemories(query);
            String context = getContext(recordIdList);
            return answer(context, query);
        }

        return defaultInteraction(query);
    }

    private String defaultInteraction(String query) {
        String prompt = "Date: " + getDateTime();
        ChatMessage systemMessage = new ChatMessage(ChatRole.SYSTEM, prompt);
        ChatMessage userMessage = new ChatMessage(ChatRole.USER, query);
        ChatCompletions chatCompletions = chatGPTService.interactWithChatGPT(GPTVersion.GPT_4, List.of(systemMessage, userMessage));
        return chatCompletions.getChoices().get(0).getMessage().getContent();
    }

    private String identifyQuery(String query) {

        String prompt = getPrompt("identifyQuery");
        ChatMessage systemMessage = new ChatMessage(ChatRole.SYSTEM, prompt);
        ChatMessage userMessage = new ChatMessage(ChatRole.USER, query);

        ChatCompletions chatCompletions = chatGPTService.interactWithChatGPT(GPTVersion.GPT_4_5, List.of(systemMessage, userMessage));
        return chatCompletions.getChoices().get(0).getMessage().getContent();
    }

    private List<Integer> getMemories(String query) {
        EmbeddingsOptions embeddingsOptions = new EmbeddingsOptions(List.of(query));
        Embeddings embedding = chatGPTService.createEmbedding(embeddingsOptions);

        CollectionSearch collectionSearch = new CollectionSearch(embedding.getData().get(0).getEmbedding(), true, true, 5);
        PointResponse point = qdrantService.getPoint("Ai-Assistant", collectionSearch);

        ArrayList<Double> scores = new ArrayList<>();
        point.getResult().forEach(match -> scores.add(match.getScore()));
        OptionalDouble averageOptional = scores.stream().mapToDouble(a -> a).average();
        if(averageOptional.isEmpty()) {
            log.error("GetContext Error");
            return Collections.emptyList();
        }
        double avarage = averageOptional.getAsDouble();

        List<Integer> recordIdList = new ArrayList<>();
        StringBuilder groupsStringBuilder = new StringBuilder();
        List<Result> results = point.getResult();
        results.forEach(result -> {
            if(result.getScore() >= avarage) {
                recordIdList.add(result.getId());
                groupsStringBuilder.append(result.getPayload().getGroup()).append(",");
            }
        });
        String groupString = groupsStringBuilder.toString();
        return recordIdList;
    }

    private String getContext(List<Integer> recordIdList) {
            StringBuilder contextBuilder = new StringBuilder();
            recordIdList.forEach(id -> {
                ResourceResponse resourceByRecordId = nocoDBService.getResourceByRecordId(id);
                String part = "\n\n\n" + resourceByRecordId.getTitle() + ": " +
                        resourceByRecordId.getDescription() +
                        "$URL: " + (resourceByRecordId.getUrl().isEmpty() ? "" : resourceByRecordId.getUrl());
                contextBuilder.append(part);
            });
            return contextBuilder.toString();
    }

    private String answer(String context, String query) {
        String systemPrompt = getPrompt("answer") +
                "context```\n" +
                context + "\n``` \n\n" +
                "Date: " + getDateTime();
        ChatMessage systemMessage = new ChatMessage(ChatRole.SYSTEM, systemPrompt);
        ChatMessage userMessage = new ChatMessage(ChatRole.USER, query);

        ChatCompletions chatCompletions = chatGPTService.interactWithChatGPT(GPTVersion.GPT_4, List.of(systemMessage, userMessage));
        return chatCompletions.getChoices().get(0).getMessage().getContent();
    }

    private ResourceResponse save(String query, String group){
        String prompt = getPrompt("save") + query + "\n```";
        ChatMessage chatMessage = new ChatMessage(ChatRole.USER, prompt);
        ChatCompletions chatCompletions = chatGPTService.interactWithChatGPT(GPTVersion.GPT_3_5, List.of(chatMessage));
        String content = chatCompletions.getChoices().get(0).getMessage().getContent();

        Gson gson = new Gson();
        Enrich enrich = gson.fromJson(content, Enrich.class);

        String finalGroup = group != null || !group.isEmpty() ? group : "memories";

        ResourceRequest resourceRequest = new ResourceRequest(
                enrich.getTitle(),
                query,
                enrich.getUrl(),
                enrich.getTags(),
                finalGroup
        );
        ResourceResponse resourceResponse = nocoDBService.addResource(resourceRequest);
        if(resourceResponse == null) {
            throw new AssistantException("nocoDB addResource response is null");
        }

        ResourceResponse rememberResponse = remember(resourceResponse.getId());
        if(rememberResponse == null) {
            throw new AssistantException("remember response is null");
        }
        return rememberResponse;
    }

    private ResourceResponse remember(int recordId) {
        ResourceResponse resourceByRecordId = nocoDBService.getResourceByRecordId(recordId);

        if(resourceByRecordId == null) {
            throw new AssistantException("nocoDB getResourceByRecordId response is null");
        }

        String title = resourceByRecordId.getTitle();
        String description = resourceByRecordId.getDescription();
        String tags = resourceByRecordId.getTags();
        String category = resourceByRecordId.getCategory();

        String query = title + ": " + description + " " + tags;
        EmbeddingsOptions embeddingsOptions = new EmbeddingsOptions(List.of(query));
        Embeddings embedding = chatGPTService.createEmbedding(embeddingsOptions);

        String finaltags = tags != null || !tags.isEmpty() ? tags : "brak";

        Payload payload = new Payload(recordId, category, finaltags);
        Point point = new Point(recordId, payload, embedding.getData().get(0).getEmbedding());
        qdrantService.addData("Ai-Assistant", List.of(point));

        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("synced", true);
        return nocoDBService.updateResource(recordId, updateMap);
    }

    public void forget(int recordId) {
        qdrantService.deletePoint("Ai-Assistant", new PointDelete(List.of(recordId)));
        nocoDBService.deleteResource(recordId);
    }

    private String getPrompt(String name) {
        JsonObject jsonObject = new JsonObject();
        try {
            JsonElement jsonElement = JsonParser.parseReader(new FileReader("prompt.json"));
            jsonElement.getAsJsonObject();
            jsonObject = jsonElement.getAsJsonObject();
        } catch (FileNotFoundException e) {
            throw new AssistantException(e.getMessage());
        }

        return  jsonObject.get(name).toString();
    }

    private String getDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault());
        return formatter.format(Instant.now());
    }
}
