package com.trello_talk.trello_talk.util;

import java.net.http.HttpResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpResponseLogger {

    public static void logResponse(HttpResponse<String> response, int statusCode) {
        String message = String.format(Constants.RESPONSE_RECEIVED_STATUS, statusCode);
        log.info(message);
        log.info(Constants.RESPONSE_BODY, response.body());
    }
}
