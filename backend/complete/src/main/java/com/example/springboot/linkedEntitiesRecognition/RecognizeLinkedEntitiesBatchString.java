package com.example.springboot.linkedEntitiesRecognition;

import com.azure.ai.textanalytics.TextAnalyticsClient;
import com.azure.ai.textanalytics.models.RecognizeLinkedEntitiesResult;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RecognizeLinkedEntitiesBatchString {
    public static String getSource(TextAnalyticsClient client, List<String> documents) {
        StringBuilder sb = new StringBuilder();
//        // The texts that need be analyzed.
//        List<String> documents = Arrays.asList(
//            "Our tour guide took us up the Space Needle during our trip to Seattle last week.",
//            "Old Faithful is a geyser at Yellowstone Park."
//        );

        // Recognizing linked entities for each document in a batch of documents
        AtomicInteger counter = new AtomicInteger();

        for (RecognizeLinkedEntitiesResult entitiesResult : client.recognizeLinkedEntitiesBatch(documents, "en", null)) {
            // Recognized linked entities from a batch of documents
            sb.append(String.format("<br>Text = %s<br>", documents.get(counter.getAndIncrement())));
            if (entitiesResult.isError()) {
                // Erroneous document
                sb.append(String.format("Cannot recognize linked entities. Error: %s<br>", entitiesResult.getError().getMessage()));
            } else {
                // Valid document
                entitiesResult.getEntities().forEach(entity -> {
                    sb.append(String.format("\tName: %s, entity ID in data source: %s, URL: %s, data source: %s.<br>",
                            entity.getName(), entity.getDataSourceEntityId(), entity.getUrl(), entity.getDataSource()));
                    entity.getMatches().forEach(entityMatch -> sb.append(String.format(
                            "\tMatched entity: %s, confidence score: %f.<br>",
                            entityMatch.getText(), entityMatch.getConfidenceScore())));
                });
            }
        }
        return sb.toString();
    }
}
