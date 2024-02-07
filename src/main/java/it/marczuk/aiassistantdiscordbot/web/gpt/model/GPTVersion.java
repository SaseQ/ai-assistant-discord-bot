package it.marczuk.aiassistantdiscordbot.web.gpt.model;

public enum GPTVersion {

    GPT_4_5("gpt-4-1106-preview"),
    GPT_4("gpt-4"),
    GPT_3_5("gpt-3.5-turbo"),
    TEXT_EMBEDDING_ADA_002("text-embedding-ada-002");

    private final String code;

    GPTVersion(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
