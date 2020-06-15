package com.example.springboot.keyPhrasesExtraction;

import com.azure.ai.textanalytics.TextAnalyticsClient;
import com.azure.ai.textanalytics.models.TextAnalyticsRequestOptions;
import com.azure.ai.textanalytics.models.TextDocumentBatchStatistics;
import com.azure.ai.textanalytics.models.TextDocumentInput;
import com.azure.ai.textanalytics.util.ExtractKeyPhrasesResultCollection;
import com.azure.core.http.rest.Response;
import com.azure.core.util.Context;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ExtractKeyPhrasesBatch {
    public static String getSource(TextAnalyticsClient client) {
        StringBuilder sb = new StringBuilder();
        // The texts that need be analyzed.
        List<TextDocumentInput> documents = Arrays.asList(
            new TextDocumentInput("A", "The food was delicious and there were wonderful staff.").setLanguage("en"),
            new TextDocumentInput("B", "Satya Nadella is the CEO of Microsoft.").setLanguage("en")
        );

        // Request options: show statistics and model version
        TextAnalyticsRequestOptions requestOptions = new TextAnalyticsRequestOptions().setIncludeStatistics(true).setModelVersion("latest");

        // Extracting key phrases for each document in a batch of documents
        Response<ExtractKeyPhrasesResultCollection> keyPhrasesBatchResultResponse =
                client.extractKeyPhrasesBatchWithResponse(documents, requestOptions, Context.NONE);

        // Response's status code
        sb.append(String.format("Status code of request response: %d%n", keyPhrasesBatchResultResponse.getStatusCode()));
        ExtractKeyPhrasesResultCollection keyPhrasesBatchResultCollection = keyPhrasesBatchResultResponse.getValue();

        // Model version
        sb.append(String.format("Results of Azure Text Analytics \"Key Phrases Extraction\" Model, version: %s%n", keyPhrasesBatchResultCollection.getModelVersion()));

        // Batch statistics
        TextDocumentBatchStatistics batchStatistics = keyPhrasesBatchResultCollection.getStatistics();
        sb.append(String.format("Documents statistics: document count = %s, erroneous document count = %s, transaction count = %s, valid document count = %s.%n",
                batchStatistics.getDocumentCount(), batchStatistics.getInvalidDocumentCount(), batchStatistics.getTransactionCount(), batchStatistics.getValidDocumentCount()));

        // Extracted key phrases for each document in a batch of documents
        AtomicInteger counter = new AtomicInteger();
        keyPhrasesBatchResultCollection.forEach(extractKeyPhraseResult -> {
            sb.append(String.format("%n%s%n", documents.get(counter.getAndIncrement())));
            if (extractKeyPhraseResult.isError()) {
                // Erroneous document
                sb.append(String.format("Cannot extract key phrases. Error: %s%n", extractKeyPhraseResult.getError().getMessage()));
            } else {
                // Valid document
                System.out.println("Extracted phrases:");
                extractKeyPhraseResult.getKeyPhrases().forEach(keyPhrases -> sb.append(String.format("\t%s.%n", keyPhrases)));
            }
        });
        return sb.toString();
    }
}
