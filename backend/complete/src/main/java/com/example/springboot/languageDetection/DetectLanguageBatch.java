package com.example.springboot.languageDetection;

import com.azure.ai.textanalytics.TextAnalyticsClient;
import com.azure.ai.textanalytics.models.DetectLanguageInput;
import com.azure.ai.textanalytics.models.DetectedLanguage;
import com.azure.ai.textanalytics.models.TextAnalyticsRequestOptions;
import com.azure.ai.textanalytics.models.TextDocumentBatchStatistics;
import com.azure.ai.textanalytics.util.DetectLanguageResultCollection;
import com.azure.core.http.rest.Response;
import com.azure.core.util.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DetectLanguageBatch {
    public static String getSource(TextAnalyticsClient client, List<String> documents) {
        StringBuilder sb = new StringBuilder();
        // The texts that need be analyzed.
        List<DetectLanguageInput> documentsBatch = new ArrayList<>();

        documents.forEach(document ->
            documentsBatch.add(new DetectLanguageInput("1", document, "US"))
        );

        // Request options: show statistics and model version
        TextAnalyticsRequestOptions requestOptions = new TextAnalyticsRequestOptions().setIncludeStatistics(true).setModelVersion("latest");

        // Detecting language for each document in a batch of documents
        Response<DetectLanguageResultCollection> detectedLanguageResultResponse = client.detectLanguageBatchWithResponse(documentsBatch, requestOptions, Context.NONE);

        // Response's status code
        sb.append(String.format("<br>Status code of request response: %d<br>", detectedLanguageResultResponse.getStatusCode()));
        DetectLanguageResultCollection detectedLanguageResultCollection = detectedLanguageResultResponse.getValue();

        // Model version
        sb.append(String.format("Results of Azure Text Analytics \"Language Detection\" Model, version: %s<br>", detectedLanguageResultCollection.getModelVersion()));

        // Batch statistics
        TextDocumentBatchStatistics batchStatistics = detectedLanguageResultCollection.getStatistics();
        sb.append(String.format("Documents statistics: document count = %s, erroneous document count = %s, transaction count = %s, valid document count = %s.<br>",
                batchStatistics.getDocumentCount(), batchStatistics.getInvalidDocumentCount(), batchStatistics.getTransactionCount(), batchStatistics.getValidDocumentCount()));

        // Detected language for each document in a batch of documents
        AtomicInteger counter = new AtomicInteger();
        detectedLanguageResultCollection.forEach(detectLanguageResult -> {
            sb.append(String.format("<br>Text = %s<br>", documents.get(counter.getAndIncrement())));
            if (detectLanguageResult.isError()) {
                // Erroneous document
                sb.append(String.format("Cannot detect language. Error: %s<br>", detectLanguageResult.getError().getMessage()));
            } else {
                // Valid document
                DetectedLanguage language = detectLanguageResult.getPrimaryLanguage();
                sb.append(String.format("Detected primary language: %s, ISO 6391 name: %s, confidence score: %f.<br>",
                        language.getName(), language.getIso6391Name(), language.getConfidenceScore()));
            }
        });
        return sb.toString();
    }
}
