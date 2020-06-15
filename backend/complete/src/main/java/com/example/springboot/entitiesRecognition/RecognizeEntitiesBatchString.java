package com.example.springboot.entitiesRecognition;

import com.azure.ai.textanalytics.TextAnalyticsClient;
import com.azure.ai.textanalytics.models.RecognizeEntitiesResult;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RecognizeEntitiesBatchString {
    public static String getSource(TextAnalyticsClient client, List<String> documents) {
        StringBuilder sb = new StringBuilder();
//        // The texts that need be analyzed.
//        List<String> documents = Arrays.asList(
//            "Satya Nadella is the CEO of Microsoft.",
//            "Elon Musk is the CEO of SpaceX and Tesla."
//        );

        // Recognizing entities for each document in a batch of documents
        AtomicInteger counter = new AtomicInteger();
        for (RecognizeEntitiesResult entitiesResult : client.recognizeEntitiesBatch(documents, "en", null)) {
            // Recognized entities for each of documents from a batch of documents
            sb.append(String.format("<br>Text = %s<br>", documents.get(counter.getAndIncrement())));
            if (entitiesResult.isError()) {
                // Erroneous document
                sb.append(String.format("Cannot recognize entities. Error: %s<br>", entitiesResult.getError().getMessage()));
            } else {
                // Valid document
                entitiesResult.getEntities().forEach(entity -> sb.append(String.format(
                        "Recognized entity: %s, entity category: %s, entity subcategory: %s, confidence score: %f.<br>",
                        entity.getText(), entity.getCategory(), entity.getSubcategory(), entity.getConfidenceScore())));
            }
        }
        return sb.toString();
    }
}
