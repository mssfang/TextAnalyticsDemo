package com.example.springboot.languageDetection;

import com.azure.ai.textanalytics.TextAnalyticsClient;
import com.azure.ai.textanalytics.models.DetectedLanguage;

public class DetectLanguage {
    public static String getSource(TextAnalyticsClient client) {
        StringBuilder sb = new StringBuilder();
        // The document that needs be analyzed.
        String document = "hello world";

        final DetectedLanguage detectedLanguage = client.detectLanguage(document);
        sb.append(String.format("Detected primary language: %s, ISO 6391 name: %s, confidence score: %f.%n",
                detectedLanguage.getName(), detectedLanguage.getIso6391Name(), detectedLanguage.getConfidenceScore()));
        return sb.toString();
    }
}
