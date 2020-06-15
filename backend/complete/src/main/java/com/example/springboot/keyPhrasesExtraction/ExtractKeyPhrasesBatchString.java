package com.example.springboot.keyPhrasesExtraction;

import com.azure.ai.textanalytics.TextAnalyticsClient;
import com.azure.ai.textanalytics.models.ExtractKeyPhraseResult;
import com.azure.ai.textanalytics.util.ExtractKeyPhrasesResultCollection;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ExtractKeyPhrasesBatchString {
    public static String getSource(TextAnalyticsClient client, List<String> documents) {
//        public static Map<String, Map<Integer, String>> getSource(TextAnalyticsClient client, List<String> documents) {

            StringBuilder sb = new StringBuilder();
//        // The texts that need be analyzed.
//        List<String> documents = Arrays.asList(
//            "The food was delicious and there were wonderful staff.",
//            "Satya Nadella is the CEO of Microsoft."
//        );
//
        // Extracting key phrases for each document in a batch of documents
        AtomicInteger counter = new AtomicInteger();
        for (ExtractKeyPhraseResult extractKeyPhraseResult : client.extractKeyPhrasesBatch(documents, "en", null)) {
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

//        // Extracting key phrases for each document in a batch of documents
//        Map<String, Map<Integer, String>> result = new HashMap<>();
//        AtomicInteger counterDocument = new AtomicInteger();
//        for (ExtractKeyPhraseResult extractKeyPhraseResult : client.extractKeyPhrasesBatch(documents, "en", null)) {
//            // Extracted key phrase for each document in a batch of documents
//
//            if (extractKeyPhraseResult.isError()) {
//                // Erroneous document
//                sb.append(String.format("Cannot extract key phrases. Error: %s<br>", extractKeyPhraseResult.getError().getMessage()));
//            } else {
//                // Valid document
//                System.out.println("<br>Extracted phrases:<br>");
//                AtomicInteger counter = new AtomicInteger();
//                Map<Integer, String> output = new HashMap<>();
//
//                extractKeyPhraseResult.getKeyPhrases().forEach(keyPhrases -> {
//                    sb.append(String.format("\t%s.<br>", keyPhrases));
//                    output.put(counter.getAndIncrement(), keyPhrases);
//                });
//
//                result.put(documents.get(counterDocument.getAndIncrement()), output);
//            }
//        }

        return sb.toString();
    }
}
