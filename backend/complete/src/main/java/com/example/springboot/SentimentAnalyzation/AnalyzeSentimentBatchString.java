package com.example.springboot.SentimentAnalyzation;

import com.azure.ai.textanalytics.TextAnalyticsClient;
import com.azure.ai.textanalytics.models.AnalyzeSentimentResult;
import com.azure.ai.textanalytics.models.DocumentSentiment;
import com.azure.ai.textanalytics.models.SentimentConfidenceScores;
import com.azure.ai.textanalytics.models.TextAnalyticsRequestOptions;
import com.azure.ai.textanalytics.models.TextDocumentBatchStatistics;
import com.azure.ai.textanalytics.util.AnalyzeSentimentResultCollection;
import com.azure.ai.textanalytics.util.ExtractKeyPhrasesResultCollection;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AnalyzeSentimentBatchString {
    public static String getSource(TextAnalyticsClient client, List<String> documents, String isIncludeStats, String modelVersion, String languageCode) {
        StringBuilder sb = new StringBuilder();
//        // The documents that need to be analyzed.
//        List<String> documents = Arrays.asList(
//            "The hotel was dark and unclean. I wouldn't recommend staying there.",
//            "The restaurant had amazing gnocchi! The waiters were excellent.",
//            "The hotel was dark and unclean. The restaurant had amazing gnocchi!"
//        );

        System.out.println("isIncludeStats=" + isIncludeStats);
        System.out.println("modelVersion= " + modelVersion);
        System.out.println("countryHint= " + languageCode);

        boolean isIncludeStatsBoolean = "true".equals(isIncludeStats) ? true : false;

        // Request options: show statistics and model version
        TextAnalyticsRequestOptions requestOptions = new TextAnalyticsRequestOptions()
                .setIncludeStatistics(isIncludeStatsBoolean)
                .setModelVersion(modelVersion);

        // Analyzing sentiment for each document in a batch of documents
        AnalyzeSentimentResultCollection analyzeSentimentResultCollection = client.analyzeSentimentBatch(documents, languageCode, requestOptions);

        // Model version
        sb.append(String.format("<br>Results of Azure Text Analytics \"Sentiment Analysis\" Model, version: %s<br>", analyzeSentimentResultCollection.getModelVersion()));

        TextDocumentBatchStatistics batchStatistics = analyzeSentimentResultCollection.getStatistics();
        if (batchStatistics != null) {
            // Batch statistics
            sb.append(String.format("<br>Documents statistics: document count = %s, erroneous document count = %s, transaction count = %s, valid document count = %s.<br>",
                    batchStatistics.getDocumentCount(), batchStatistics.getInvalidDocumentCount(), batchStatistics.getTransactionCount(), batchStatistics.getValidDocumentCount()));
        }

        AtomicInteger counter = new AtomicInteger();
        for (AnalyzeSentimentResult analyzeSentimentResult : analyzeSentimentResultCollection) {
            sb.append(String.format("<br>Text = %s<br>", documents.get(counter.getAndIncrement())));
            if (analyzeSentimentResult.isError()) {
                // Erroneous document
                sb.append(String.format("Cannot analyze sentiment. Error: %s<br>", analyzeSentimentResult.getError().getMessage()));
            } else {
                // Valid document
                DocumentSentiment documentSentiment = analyzeSentimentResult.getDocumentSentiment();
                SentimentConfidenceScores scores = documentSentiment.getConfidenceScores();
                sb.append(String.format("Analyzed document sentiment: %s, positive score: %f, neutral score: %f, negative score: %f.<br>",
                        documentSentiment.getSentiment(), scores.getPositive(), scores.getNeutral(), scores.getNegative()));
                documentSentiment.getSentences().forEach(sentenceSentiment -> {
                    SentimentConfidenceScores sentenceScores = sentenceSentiment.getConfidenceScores();
                    sb.append(String.format(
                            "\tAnalyzed sentence sentiment: %s, positive score: %f, neutral score: %f, negative score: %f.<br>",
                            sentenceSentiment.getSentiment(), sentenceScores.getPositive(), sentenceScores.getNeutral(), sentenceScores.getNegative()));
                });
            }
        }
        return sb.toString();
    }
}
