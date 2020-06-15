package com.example.springboot.linkedEntitiesRecognition;

import com.azure.ai.textanalytics.TextAnalyticsClient;

public class RecognizeLinkedEntities {
    public static String getSource(TextAnalyticsClient client) {
        StringBuilder sb = new StringBuilder();
        // The document that needs be analyzed.
        String document = "Old Faithful is a geyser at Yellowstone Park.";

        client.recognizeLinkedEntities(document).forEach(linkedEntity -> {
            System.out.println("Linked Entities:");
            sb.append(String.format("Name: %s, entity ID in data source: %s, URL: %s, data source: %s.%n",
                    linkedEntity.getName(), linkedEntity.getDataSourceEntityId(), linkedEntity.getUrl(),
                    linkedEntity.getDataSource()));
            linkedEntity.getMatches().forEach(entityMatch -> sb.append(String.format(
                    "Matched entity: %s, confidence score: %f.%n",
                    entityMatch.getText(), entityMatch.getConfidenceScore())));
        });
        return sb.toString();
    }
}
