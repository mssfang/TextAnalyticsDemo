package com.example.springboot.SentimentAnalyzation;

import com.azure.ai.textanalytics.TextAnalyticsClient;
import com.azure.ai.textanalytics.models.DocumentSentiment;
import com.azure.ai.textanalytics.models.SentimentConfidenceScores;

public class AnalyzeSentiment {
    public static String getSource(TextAnalyticsClient client) {
        StringBuilder sb = new StringBuilder();
        // The text that needs be analyzed.
        String document = "The hotel was dark and unclean. I like Microsoft.";

        final DocumentSentiment documentSentiment = client.analyzeSentiment(document);
        SentimentConfidenceScores scores = documentSentiment.getConfidenceScores();
        sb.append(String.format(
                "Recognized document sentiment: %s, positive score: %f, neutral score: %f, negative score: %f.%n",
                documentSentiment.getSentiment(), scores.getPositive(), scores.getNeutral(), scores.getNegative()));

        documentSentiment.getSentences().forEach(sentenceSentiment -> {
            SentimentConfidenceScores sentenceScores = sentenceSentiment.getConfidenceScores();
            sb.append(String.format("Recognized sentence sentiment: %s, positive score: %f, neutral score: %f, negative score: %f.%n",
                    sentenceSentiment.getSentiment(), sentenceScores.getPositive(), sentenceScores.getNeutral(), sentenceScores.getNegative()));
        });
        return sb.toString();
    }
}
