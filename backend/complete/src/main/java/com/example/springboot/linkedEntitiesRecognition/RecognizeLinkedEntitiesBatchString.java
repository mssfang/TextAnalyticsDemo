package com.example.springboot.linkedEntitiesRecognition;

import com.azure.ai.textanalytics.TextAnalyticsClient;
import com.azure.ai.textanalytics.models.RecognizeLinkedEntitiesResult;
import com.azure.ai.textanalytics.models.TextAnalyticsRequestOptions;
import com.azure.ai.textanalytics.models.TextDocumentBatchStatistics;
import com.azure.ai.textanalytics.util.ExtractKeyPhrasesResultCollection;
import com.azure.ai.textanalytics.util.RecognizeEntitiesResultCollection;
import com.azure.ai.textanalytics.util.RecognizeLinkedEntitiesResultCollection;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RecognizeLinkedEntitiesBatchString {
    public static String getSource(TextAnalyticsClient client, List<String> documents, String isIncludeStats, String modelVersion, String languageCode) {
        StringBuilder sb = new StringBuilder();
//        // The texts that need be analyzed.
//        List<String> documents = Arrays.asList(
//            "Our tour guide took us up the Space Needle during our trip to Seattle last week.",
//            "Old Faithful is a geyser at Yellowstone Park."
//        );

        System.out.println("isIncludeStats=" + isIncludeStats);
        System.out.println("modelVersion= " + modelVersion);
        System.out.println("countryHint= " + languageCode);

        boolean isIncludeStatsBoolean = "true".equals(isIncludeStats) ? true : false;

        // Request options: show statistics and model version
        TextAnalyticsRequestOptions requestOptions = new TextAnalyticsRequestOptions()
                .setIncludeStatistics(isIncludeStatsBoolean)
                .setModelVersion(modelVersion);

        // Recognizing linked entities for each document in a batch of documents
        RecognizeLinkedEntitiesResultCollection recognizeLinkedEntitiesResultCollection = client.recognizeLinkedEntitiesBatch(documents, languageCode, requestOptions);

        // Model version
        sb.append(String.format("<br>Results of Azure Text Analytics \"Linked Entities Recognition\" Model, version: %s<br>", recognizeLinkedEntitiesResultCollection.getModelVersion()));

        // Batch statistics
        TextDocumentBatchStatistics batchStatistics = recognizeLinkedEntitiesResultCollection.getStatistics();
        if (batchStatistics != null) {
            sb.append(String.format("<br>Documents statistics: document count = %s, erroneous document count = %s, transaction count = %s, valid document count = %s.<br>",
                batchStatistics.getDocumentCount(), batchStatistics.getInvalidDocumentCount(), batchStatistics.getTransactionCount(), batchStatistics.getValidDocumentCount()));
        }

        AtomicInteger counter = new AtomicInteger();
        for (RecognizeLinkedEntitiesResult entitiesResult : recognizeLinkedEntitiesResultCollection) {
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
