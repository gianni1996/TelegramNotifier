package com.telegram_notifier.telegram_notifier.util;

public class JsonParser {
    public static String extractField(String json, String fieldName) {
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
