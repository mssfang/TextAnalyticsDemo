package com.example.springboot.linkedEntitiesRecognition;

import com.azure.ai.textanalytics.TextAnalyticsClient;
import com.azure.ai.textanalytics.models.TextAnalyticsRequestOptions;
import com.azure.ai.textanalytics.models.TextDocumentBatchStatistics;
import com.azure.ai.textanalytics.models.TextDocumentInput;
import com.azure.ai.textanalytics.util.RecognizeLinkedEntitiesResultCollection;
import com.azure.core.http.rest.Response;
import com.azure.core.util.Context;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RecognizeLinkedEntitiesBatch {
    public static String getSource(TextAnalyticsClient client) {
        StringBuilder sb = new StringBuilder();
        // The texts that need be analyzed.
        List<TextDocumentInput> documents = Arrays.asList(
            new TextDocumentInput("A", "Our tour guide took us up the Space Needle during our trip to Seattle last week.").setLanguage("en"),
            new TextDocumentInput("B", "Old Faithful is a geyser at Yellowstone Park.").setLanguage("en")
        );

        // Request options: show statistics and model version
        TextAnalyticsRequestOptions requestOptions = new TextAnalyticsRequestOptions().setIncludeStatistics(true).setModelVersion("latest");

        Response<RecognizeLinkedEntitiesResultCollection> linkedEntitiesBatchResultResponse =
                client.recognizeLinkedEntitiesBatchWithResponse(documents, requestOptions, Context.NONE);

        // Response's status code
        sb.append(String.format("Status code of request response: %d%n", linkedEntitiesBatchResultResponse.getStatusCode()));
        RecognizeLinkedEntitiesResultCollection linkedEntitiesResultCollection = linkedEntitiesBatchResultResponse.getValue();

        // Model version
        sb.append(String.format("Results of Azure Text Analytics \"Linked Entities Recognition\" Model, version: %s%n", linkedEntitiesResultCollection.getModelVersion()));

        // Batch statistics
        TextDocumentBatchStatistics batchStatistics = linkedEntitiesResultCollection.getStatistics();
        sb.append(String.format("Documents statistics: document count = %s, erroneous document count = %s, transaction count = %s, valid document count = %s.%n",
                batchStatistics.getDocumentCount(), batchStatistics.getInvalidDocumentCount(), batchStatistics.getTransactionCount(), batchStatistics.getValidDocumentCount()));

        // Recognizing linked entities for each document in a batch of documents
        AtomicInteger counter = new AtomicInteger();
        linkedEntitiesResultCollection.forEach(entitiesResult -> {
            // Recognized linked entities from documents
            sb.append(String.format("%n%s%n", documents.get(counter.getAndIncrement())));
            if (entitiesResult.isError()) {
                // Erroneous document
                sb.append(String.format("Cannot recognize linked entities. Error: %s%n", entitiesResult.getError().getMessage()));
            } else {
                // Valid document
                entitiesResult.getEntities().forEach(linkedEntity -> {
                    System.out.println("Linked Entities:");
                    sb.append(String.format("\tName: %s, entity ID in data source: %s, URL: %s, data source: %s.%n",
                            linkedEntity.getName(), linkedEntity.getDataSourceEntityId(), linkedEntity.getUrl(), linkedEntity.getDataSource()));
                    linkedEntity.getMatches().forEach(entityMatch -> sb.append(String.format(
                            "\tMatched entity: %s, confidence score: %f.%n",
                            entityMatch.getText(), entityMatch.getConfidenceScore())));
                });
            }
        });
        return sb.toString();
    }
}
