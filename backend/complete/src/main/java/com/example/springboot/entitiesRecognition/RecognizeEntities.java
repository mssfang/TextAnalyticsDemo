package com.example.springboot.entitiesRecognition;

import com.azure.ai.textanalytics.TextAnalyticsClient;

public class RecognizeEntities {
    public static String getSource(TextAnalyticsClient client) {
        StringBuilder sb = new StringBuilder();
        // The document that needs be analyzed.
        String document = "Satya Nadella is the CEO of Microsoft";

        client.recognizeEntities(document).forEach(entity -> sb.append(String.format(
                "Recognized categorized entity: %s, entity category: %s, entity subcategory: %s, confidence score: %f.%n",
                entity.getText(), entity.getCategory(), entity.getSubcategory(), entity.getConfidenceScore())));
        return sb.toString();
    }
}
