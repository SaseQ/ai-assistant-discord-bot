package it.marczuk.aiassistantdiscordbot.web.gpt.exception;

public class BadRequestToRestTemplateException extends RuntimeException {

    public BadRequestToRestTemplateException(String url, String errorStatus) {
        super("Error request to: " + url + ", Error status: " + errorStatus);
    }
}
