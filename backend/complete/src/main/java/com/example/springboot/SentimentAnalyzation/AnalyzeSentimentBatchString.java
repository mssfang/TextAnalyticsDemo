package com.example.springboot.SentimentAnalyzation;

import com.azure.ai.textanalytics.TextAnalyticsClient;
import com.azure.ai.textanalytics.models.AnalyzeSentimentResult;
import com.azure.ai.textanalytics.models.DocumentSentiment;
import com.azure.ai.textanalytics.models.SentimentConfidenceScores;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AnalyzeSentimentBatchString {
    public static String getSource(TextAnalyticsClient client, List<String> documents) {
        StringBuilder sb = new StringBuilder();
//        // The documents that need to be analyzed.
//        List<String> documents = Arrays.asList(
//            "The hotel was dark and unclean. I wouldn't recommend staying there.",
//            "The restaurant had amazing gnocchi! The waiters were excellent.",
//            "The hotel was dark and unclean. The restaurant had amazing gnocchi!"
//        );

        // Analyzed sentiment for each document in a batch of documents
        AtomicInteger counter = new AtomicInteger();
        for (AnalyzeSentimentResult analyzeSentimentResult : client.analyzeSentimentBatch(documents, "en", null)) {
            sb.append(String.format("<br>Text = %s<br>", documents.get(counter.getAndIncrement())));
            if (analyzeSentimentResult.isError()) {
                // Erroneous document
                sb.append(sb.append(String.format("Cannot analyze sentiment. Error: %s<br>", analyzeSentimentResult.getError().getMessage())));
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
