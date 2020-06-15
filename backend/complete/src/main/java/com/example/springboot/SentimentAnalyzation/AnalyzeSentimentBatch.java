package com.example.springboot.SentimentAnalyzation;

import com.azure.ai.textanalytics.TextAnalyticsClient;
import com.azure.ai.textanalytics.models.DocumentSentiment;
import com.azure.ai.textanalytics.models.SentimentConfidenceScores;
import com.azure.ai.textanalytics.models.TextAnalyticsRequestOptions;
import com.azure.ai.textanalytics.models.TextDocumentBatchStatistics;
import com.azure.ai.textanalytics.models.TextDocumentInput;
import com.azure.ai.textanalytics.util.AnalyzeSentimentResultCollection;
import com.azure.core.http.rest.Response;
import com.azure.core.util.Context;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AnalyzeSentimentBatch {

    public static String getSource(TextAnalyticsClient client) {
        StringBuilder sb = new StringBuilder();
        List<TextDocumentInput> documents = Arrays.asList(
            new TextDocumentInput("A", "The hotel was dark and unclean. I wouldn't recommend staying there.").setLanguage("en"),
            new TextDocumentInput("B", "The restaurant had amazing gnocchi! The waiters were excellent.").setLanguage("en"),
            new TextDocumentInput("C", "The hotel was dark and unclean. The restaurant had amazing gnocchi!").setLanguage("en")
        );

        // Request options: show statistics and model version
        TextAnalyticsRequestOptions requestOptions = new TextAnalyticsRequestOptions().setIncludeStatistics(true).setModelVersion("latest");

        // Analyzing sentiment for each document in a batch of documents
        Response<AnalyzeSentimentResultCollection> sentimentBatchResultResponse =
                client.analyzeSentimentBatchWithResponse(documents, requestOptions, Context.NONE);

        // Response's status code
        sb.append(String.format("Status code of request response: %d%n", sentimentBatchResultResponse.getStatusCode()));
        AnalyzeSentimentResultCollection sentimentBatchResultCollection = sentimentBatchResultResponse.getValue();

        // Model version
        sb.append(String.format("Results of Azure Text Analytics \"Sentiment Analysis\" Model, version: %s%n", sentimentBatchResultCollection.getModelVersion()));

        // Batch statistics
        TextDocumentBatchStatistics batchStatistics = sentimentBatchResultCollection.getStatistics();
        sb.append(String.format("Documents statistics: document count = %s, erroneous document count = %s, transaction count = %s, valid document count = %s.%n",
                batchStatistics.getDocumentCount(), batchStatistics.getInvalidDocumentCount(), batchStatistics.getTransactionCount(), batchStatistics.getValidDocumentCount()));

        // Analyzed sentiment for each document in a batch of documents
        AtomicInteger counter = new AtomicInteger();
        sentimentBatchResultCollection.forEach(analyzeSentimentResult -> {
            sb.append(String.format("%n%s%n", documents.get(counter.getAndIncrement())));
            if (analyzeSentimentResult.isError()) {
                // Erroneous document
                sb.append(String.format("Cannot analyze sentiment. Error: %s%n", analyzeSentimentResult.getError().getMessage()));
            } else {
                // Valid document
                DocumentSentiment documentSentiment = analyzeSentimentResult.getDocumentSentiment();
                SentimentConfidenceScores scores = documentSentiment.getConfidenceScores();
                sb.append(String.format("Analyzed document sentiment: %s, positive score: %f, neutral score: %f, negative score: %f.%n",
                        documentSentiment.getSentiment(), scores.getPositive(), scores.getNeutral(), scores.getNegative()));
                documentSentiment.getSentences().forEach(sentenceSentiment -> {
                    SentimentConfidenceScores sentenceScores = sentenceSentiment.getConfidenceScores();
                    sb.append(String.format(
                            "\tAnalyzed sentence sentiment: %s, positive score: %f, neutral score: %f, negative score: %f.%n",
                            sentenceSentiment.getSentiment(), sentenceScores.getPositive(), sentenceScores.getNeutral(), sentenceScores.getNegative()));
                });
            }
        });

        return sb.toString();
    }
}
