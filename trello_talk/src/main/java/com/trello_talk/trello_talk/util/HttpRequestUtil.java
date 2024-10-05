package com.trello_talk.trello_talk.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.trello_talk.trello_talk.config.error.ApiException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpRequestUtil {

    // Metodo per inviare una richiesta HTTP GET
    public static String sendGetRequest(String urlString) {
        try {
            // Crea connessione HTTP
            HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
            connection.setRequestMethod("GET");  // Imposta il metodo di richiesta

            // Controlla se la risposta è OK (200)
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Leggi la risposta dall'input stream
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return response.toString();  // Restituisce la risposta come stringa
            } else {
                // Log se la risposta non è OK
                log.info("Errore nella richiesta, codice di stato: {}", connection.getResponseCode());
                throw new ApiException("Errore nella richiesta: " + connection.getResponseCode());
            }
        } catch (Exception e) {
            // Gestione dell'eccezione e logging
            log.info("Errore durante l'invio della richiesta: {}", e.getMessage(), e);
            throw new ApiException("Si è verificato un errore durante l'invio della richiesta: " + e.getMessage(), e);
        }
    }
}
