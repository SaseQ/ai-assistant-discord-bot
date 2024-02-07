package it.marczuk.aiassistantdiscordbot.web.rest.exception;

public class BadRequestToRestTemplateException extends RuntimeException {

    public BadRequestToRestTemplateException(String url, String errorStatus) {
        super("Error request to: " + url + ", Error status: " + errorStatus);
    }
}
