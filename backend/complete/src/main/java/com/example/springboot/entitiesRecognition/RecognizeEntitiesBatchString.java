package com.example.springboot.entitiesRecognition;

import com.azure.ai.textanalytics.TextAnalyticsClient;
import com.azure.ai.textanalytics.models.RecognizeEntitiesResult;
import com.azure.ai.textanalytics.models.TextAnalyticsRequestOptions;
import com.azure.ai.textanalytics.models.TextDocumentBatchStatistics;
import com.azure.ai.textanalytics.util.ExtractKeyPhrasesResultCollection;
import com.azure.ai.textanalytics.util.RecognizeEntitiesResultCollection;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RecognizeEntitiesBatchString {
    public static String getSource(TextAnalyticsClient client, List<String> documents, String isIncludeStats, String modelVersion, String languageCode) {
        StringBuilder sb = new StringBuilder();
//        // The texts that need be analyzed.
//        List<String> documents = Arrays.asList(
//            "Satya Nadella is the CEO of Microsoft.",
//            "Elon Musk is the CEO of SpaceX and Tesla."
//        );

        System.out.println("isIncludeStats=" + isIncludeStats);
        System.out.println("modelVersion= " + modelVersion);
        System.out.println("countryHint= " + languageCode);

        boolean isIncludeStatsBoolean = "true".equals(isIncludeStats) ? true : false;

        // Request options: show statistics and model version
        TextAnalyticsRequestOptions requestOptions = new TextAnalyticsRequestOptions()
                .setIncludeStatistics(isIncludeStatsBoolean)
                .setModelVersion(modelVersion);

        // Recognizing entities for each of documents from a batch of documents
        RecognizeEntitiesResultCollection recognizeEntitiesResultCollection = client.recognizeEntitiesBatch(documents, languageCode, requestOptions);
        // Model version
        sb.append(String.format("<br>Results of Azure Text Analytics \"Entities Recognition\" Model, version: %s<br>", recognizeEntitiesResultCollection.getModelVersion()));

        // Batch statistics
        TextDocumentBatchStatistics batchStatistics = recognizeEntitiesResultCollection.getStatistics();
        if (batchStatistics != null) {
            sb.append(String.format("<br>Documents statistics: document count = %s, erroneous document count = %s, transaction count = %s, valid document count = %s.<br>",
                    batchStatistics.getDocumentCount(), batchStatistics.getInvalidDocumentCount(), batchStatistics.getTransactionCount(), batchStatistics.getValidDocumentCount()));
        }

        AtomicInteger counter = new AtomicInteger();
        for (RecognizeEntitiesResult entitiesResult : recognizeEntitiesResultCollection) {
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
