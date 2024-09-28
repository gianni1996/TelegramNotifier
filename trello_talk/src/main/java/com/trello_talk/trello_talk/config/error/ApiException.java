package com.trello_talk.trello_talk.config.error;

public class ApiException extends RuntimeException {
    
    public ApiException(String message) {
        super(message);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
