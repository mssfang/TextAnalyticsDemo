package com.example.springboot.entitiesRecognition;

import com.azure.ai.textanalytics.TextAnalyticsClient;
import com.azure.ai.textanalytics.models.TextAnalyticsRequestOptions;
import com.azure.ai.textanalytics.models.TextDocumentBatchStatistics;
import com.azure.ai.textanalytics.models.TextDocumentInput;
import com.azure.ai.textanalytics.util.RecognizeEntitiesResultCollection;
import com.azure.core.http.rest.Response;
import com.azure.core.util.Context;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RecognizeEntitiesBatch {
    public static String getSource(TextAnalyticsClient client) {
        StringBuilder sb = new StringBuilder();
        // The texts that need be analyzed.
        List<TextDocumentInput> documents = Arrays.asList(
            new TextDocumentInput("A", "Satya Nadella is the CEO of Microsoft.").setLanguage("en"),
            new TextDocumentInput("B", "Elon Musk is the CEO of SpaceX and Tesla.").setLanguage("en")
        );

        // Request options: show statistics and model version
        TextAnalyticsRequestOptions requestOptions = new TextAnalyticsRequestOptions().setIncludeStatistics(true).setModelVersion("latest");

        // Recognizing entities for each document in a batch of documents
        Response<RecognizeEntitiesResultCollection> entitiesBatchResultResponse =
                client.recognizeEntitiesBatchWithResponse(documents, requestOptions, Context.NONE);

        // Response's status code
        sb.append(String.format("Status code of request response: %d%n", entitiesBatchResultResponse.getStatusCode()));
        RecognizeEntitiesResultCollection recognizeEntitiesResultCollection = entitiesBatchResultResponse.getValue();

        // Model version
        sb.append(String.format("Results of Azure Text Analytics \"Entities Recognition\" Model, version: %s%n", recognizeEntitiesResultCollection.getModelVersion()));

        // Batch statistics
        TextDocumentBatchStatistics batchStatistics = recognizeEntitiesResultCollection.getStatistics();
        sb.append(String.format("Documents statistics: document count = %s, erroneous document count = %s, transaction count = %s, valid document count = %s.%n",
                batchStatistics.getDocumentCount(), batchStatistics.getInvalidDocumentCount(), batchStatistics.getTransactionCount(), batchStatistics.getValidDocumentCount()));

        // Recognized entities for each document in a batch of documents
        AtomicInteger counter = new AtomicInteger();
        recognizeEntitiesResultCollection.forEach(entitiesResult -> {
            // Recognized entities for each document in a batch of documents
            sb.append(String.format("%n%s%n", documents.get(counter.getAndIncrement())));
            if (entitiesResult.isError()) {
                // Erroneous document
                sb.append(String.format("Cannot recognize entities. Error: %s%n", entitiesResult.getError().getMessage()));
            } else {
                // Valid document
                entitiesResult.getEntities().forEach(entity -> sb.append(String.format(
                        "Recognized entity: %s, entity category: %s, entity subcategory: %s, confidence score: %f.%n",
                        entity.getText(), entity.getCategory(), entity.getSubcategory(), entity.getConfidenceScore())));
            }
        });
        return sb.toString();
    }
}
