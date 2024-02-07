package it.marczuk.aiassistantdiscordbot.web.gpt.service;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.*;
import com.azure.core.credential.KeyCredential;
import it.marczuk.aiassistantdiscordbot.web.gpt.configuration.ChatGPTConfig;
import it.marczuk.aiassistantdiscordbot.web.gpt.model.GPTVersion;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class ChatGPTService {

    private final ChatGPTConfig chatGPTConfig;
    private OpenAIClient client;

    public ChatGPTService(ChatGPTConfig chatGPTConfig) {
        this.chatGPTConfig = chatGPTConfig;
    }

    @PostConstruct
    public void init() {
        this.client = new OpenAIClientBuilder()
                .credential(new KeyCredential(chatGPTConfig.getApiKey()))
                .buildClient();
    }

    public ChatCompletions interactWithChatGPT(GPTVersion gptVersion, List<ChatMessage> messages) {
        ChatCompletionsOptions options = new ChatCompletionsOptions(messages);
        return client.getChatCompletions(gptVersion.getCode(), options);
    }

    public Embeddings createEmbedding(EmbeddingsOptions embeddingsOptions) {
        return client.getEmbeddings(GPTVersion.TEXT_EMBEDDING_ADA_002.getCode(), embeddingsOptions);
    }
}
