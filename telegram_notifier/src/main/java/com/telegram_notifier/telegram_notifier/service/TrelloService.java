package com.telegram_notifier.telegram_notifier.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.telegram_notifier.telegram_notifier.config.TrelloConfig;
import com.telegram_notifier.telegram_notifier.model.TrelloCard;
import com.telegram_notifier.telegram_notifier.model.TrelloList;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TrelloService {

    @Autowired
    private TrelloConfig trelloConfig;

    public List<TrelloList> getLists(String boardId) {
        List<TrelloList> lists = new ArrayList<>();
        try {
            String url = "https://api.trello.com/1/boards/" + boardId + "/lists?key=" + trelloConfig.getApiKey()
                    + "&token=" + trelloConfig.getOauthToken();
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                lists = parseLists(response.toString());
            } else {
                log.error("Errore nella richiesta: {}", connection.getResponseCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lists;
    }

    public List<TrelloCard> getCardsInList(String listId) {
        List<TrelloCard> cards = new ArrayList<>();
        try {
            String url = "https://api.trello.com/1/lists/" + listId + "/cards?fields=id,name,desc&key="
                    + trelloConfig.getApiKey() + "&token=" + trelloConfig.getOauthToken();
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                String[] cardObjects = response.toString().split("\\},\\{");
                for (String cardObject : cardObjects) {
                    String id = extractField(cardObject, "id");
                    String name = extractField(cardObject, "name");
                    String description = extractField(cardObject, "desc");
                    cards.add(new TrelloCard(id, name, description));
                }
            } else {
                log.error("Errore nella richiesta delle card: " + connection.getResponseCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cards;
    }

    // Metodo per parsare le liste
    private List<TrelloList> parseLists(String jsonResponse) {
        List<TrelloList> lists = new ArrayList<>();
        String[] listObjects = jsonResponse.split("\\},\\{");
        for (String listObject : listObjects) {
            String id = extractField(listObject, "id");
            String name = extractField(listObject, "name");
            lists.add(new TrelloList(id, name));
        }
        return lists;
    }

    // Metodo per estrarre un campo dal JSON
    private String extractField(String json, String fieldName) {
        String searchString = "\"" + fieldName + "\":\"";
        int startIndex = json.indexOf(searchString);

        if (startIndex == -1) {
            return "";
        }

        startIndex += searchString.length();
        int endIndex = json.indexOf("\"", startIndex);

        if (endIndex == -1 || endIndex < startIndex) {
            return "";
        }

        return json.substring(startIndex, endIndex);
    }
}
