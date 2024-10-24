package com.trello_talk.trello_talk.config.http;

import java.net.URI;
import java.net.http.HttpRequest;

import org.springframework.stereotype.Component;

import com.trello_talk.trello_talk.util.Constants;

@Component
public class HttpRequestBuilder {

        public HttpRequest buildGetRequest(String url) {
                return HttpRequest.newBuilder()
                                .uri(URI.create(url))
                                .GET()
                                .header(Constants.HEADER_ACCEPT, Constants.CONTENT_TYPE_JSON)                                                                                         
                                .build();
        }

        public HttpRequest buildPostRequest(String url) {
                return HttpRequest.newBuilder()
                                .uri(URI.create(url))
                                .POST(HttpRequest.BodyPublishers.noBody())
                                .header(Constants.HEADER_ACCEPT, Constants.CONTENT_TYPE_JSON)
                                .build();
        }

        public HttpRequest buildPutRequest(String url) {
                return HttpRequest.newBuilder()
                                .uri(URI.create(url))
                                .PUT(HttpRequest.BodyPublishers.noBody())
                                .header(Constants.HEADER_ACCEPT, Constants.CONTENT_TYPE_JSON)
                                .build();
        }

        public HttpRequest buildDeleteRequest(String url) {
                return HttpRequest.newBuilder()
                                .uri(URI.create(url))
                                .DELETE()
                                .header(Constants.HEADER_ACCEPT, Constants.CONTENT_TYPE_JSON)
                                .build();
        }
}
