package com.example.springboot.languageDetection;

import com.azure.ai.textanalytics.TextAnalyticsClient;
import com.azure.ai.textanalytics.models.DetectedLanguage;
import com.azure.ai.textanalytics.models.TextAnalyticsRequestOptions;
import com.azure.ai.textanalytics.models.TextDocumentBatchStatistics;
import com.azure.ai.textanalytics.util.DetectLanguageResultCollection;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DetectLanguageBatchString {
    public static String getSource(TextAnalyticsClient client, List<String> documents) {
        StringBuilder sb = new StringBuilder();
//        // The texts that need be analyzed.
//        List<String> documents = Arrays.asList(
//            "This is written in English.",
//            "Este es un documento  escrito en Español.",
//            "这是用英语写的.",
//            "これは英語で書かれています。"
//        );

        // Request options: show statistics and model version
        TextAnalyticsRequestOptions requestOptions = new TextAnalyticsRequestOptions().setIncludeStatistics(true).setModelVersion("latest");

        // Detecting language for each document in a batch of documents
        AtomicInteger counter = new AtomicInteger();
        DetectLanguageResultCollection detectedLanguageResultCollection = client.detectLanguageBatch(documents, "US", requestOptions);
        // Model version
        sb.append(String.format("<br>Results of Azure Text Analytics \"Language Detection\" Model, version: %s<br>", detectedLanguageResultCollection.getModelVersion()));

        // Batch statistics
        TextDocumentBatchStatistics batchStatistics = detectedLanguageResultCollection.getStatistics();
        sb.append(String.format("<br>Documents statistics: document count = %s, erroneous document count = %s, transaction count = %s, valid document count = %s.<br>",
                batchStatistics.getDocumentCount(), batchStatistics.getInvalidDocumentCount(), batchStatistics.getTransactionCount(), batchStatistics.getValidDocumentCount()));

        detectedLanguageResultCollection.forEach(detectLanguageResult -> {
            // Detected language for each document
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
