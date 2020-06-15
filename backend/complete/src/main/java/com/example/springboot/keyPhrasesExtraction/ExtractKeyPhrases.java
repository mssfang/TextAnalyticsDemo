package com.example.springboot.keyPhrasesExtraction;

import com.azure.ai.textanalytics.TextAnalyticsClient;

public class ExtractKeyPhrases {
    public static String getSource(TextAnalyticsClient client) {
        StringBuilder sb = new StringBuilder();
        // The document that needs be analyzed.
        String document = "The food was delicious and there were wonderful staff.";

        System.out.println("Extracted phrases:");
        client.extractKeyPhrases(document).forEach(keyPhrase -> sb.append(String.format("%s.%n", keyPhrase)));
        return sb.toString();
    }
}
