package com.example.springboot.keyPhrasesExtraction;

import com.azure.ai.textanalytics.TextAnalyticsClient;
import com.azure.ai.textanalytics.models.ExtractKeyPhraseResult;
import com.azure.ai.textanalytics.models.TextAnalyticsRequestOptions;
import com.azure.ai.textanalytics.models.TextDocumentBatchStatistics;
import com.azure.ai.textanalytics.util.DetectLanguageResultCollection;
import com.azure.ai.textanalytics.util.ExtractKeyPhrasesResultCollection;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ExtractKeyPhrasesBatchString {
    public static String getSource(TextAnalyticsClient client, List<String> documents, String isIncludeStats, String modelVersion, String languageCode) {

        StringBuilder sb = new StringBuilder();
//        // The texts that need be analyzed.
//        List<String> documents = Arrays.asList(
//            "The food was delicious and there were wonderful staff.",
//            "Satya Nadella is the CEO of Microsoft."
//        );


        System.out.println("isIncludeStats=" + isIncludeStats);
        System.out.println("modelVersion= " + modelVersion);
        System.out.println("countryHint= " + languageCode);

        boolean isIncludeStatsBoolean = "true".equals(isIncludeStats) ? true : false;

        // Request options: show statistics and model version
        TextAnalyticsRequestOptions requestOptions = new TextAnalyticsRequestOptions()
                .setIncludeStatistics(isIncludeStatsBoolean)
                .setModelVersion(modelVersion);

        // Extracting key phrases for each document in a batch of documents
        ExtractKeyPhrasesResultCollection extractKeyPhrasesResultCollection = client.extractKeyPhrasesBatch(documents, languageCode, requestOptions);
        // Model version
        sb.append(String.format("<br>Results of Azure Text Analytics \"Key Phrases Extraction\" Model, version: %s<br>", extractKeyPhrasesResultCollection.getModelVersion()));

        TextDocumentBatchStatistics batchStatistics = extractKeyPhrasesResultCollection.getStatistics();
        if (batchStatistics != null) {
            // Batch statistics
            sb.append(String.format("<br>Documents statistics: document count = %s, erroneous document count = %s, transaction count = %s, valid document count = %s.<br>",
                    batchStatistics.getDocumentCount(), batchStatistics.getInvalidDocumentCount(), batchStatistics.getTransactionCount(), batchStatistics.getValidDocumentCount()));
        }

        AtomicInteger counter = new AtomicInteger();
        for (ExtractKeyPhraseResult extractKeyPhraseResult : extractKeyPhrasesResultCollection) {
            // Extracted key phrase for each document in a batch of documents
            sb.append(String.format("<br>Text = %s<br>", documents.get(counter.getAndIncrement())));
            if (extractKeyPhraseResult.isError()) {
                // Erroneous document
                sb.append(String.format("Cannot extract key phrases. Error: %s<br>", extractKeyPhraseResult.getError().getMessage()));
            } else {
                // Valid document
                System.out.println("Extracted phrases:<br>");
                extractKeyPhraseResult.getKeyPhrases().forEach(keyPhrases -> sb.append(String.format("\t%s.<br>", keyPhrases)));
            }
        }
        return sb.toString();
    }
}
