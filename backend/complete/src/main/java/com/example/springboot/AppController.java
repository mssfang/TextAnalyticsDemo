package com.example.springboot;

import com.azure.ai.textanalytics.TextAnalyticsClient;
import com.azure.ai.textanalytics.TextAnalyticsClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.util.Configuration;
import com.example.springboot.SentimentAnalyzation.AnalyzeSentiment;
import com.example.springboot.SentimentAnalyzation.AnalyzeSentimentBatch;
import com.example.springboot.SentimentAnalyzation.AnalyzeSentimentBatchString;
import com.example.springboot.entitiesRecognition.RecognizeEntities;
import com.example.springboot.entitiesRecognition.RecognizeEntitiesBatch;
import com.example.springboot.entitiesRecognition.RecognizeEntitiesBatchString;
import com.example.springboot.keyPhrasesExtraction.ExtractKeyPhrases;
import com.example.springboot.keyPhrasesExtraction.ExtractKeyPhrasesBatch;
import com.example.springboot.keyPhrasesExtraction.ExtractKeyPhrasesBatchString;
import com.example.springboot.languageDetection.DetectLanguage;
import com.example.springboot.languageDetection.DetectLanguageBatch;
import com.example.springboot.languageDetection.DetectLanguageBatchString;
import com.example.springboot.linkedEntitiesRecognition.RecognizeLinkedEntities;
import com.example.springboot.linkedEntitiesRecognition.RecognizeLinkedEntitiesBatch;
import com.example.springboot.linkedEntitiesRecognition.RecognizeLinkedEntitiesBatchString;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@CrossOrigin
@RestController
public class AppController {
	TextAnalyticsClient client = new TextAnalyticsClientBuilder()
			.credential(new AzureKeyCredential(Configuration.getGlobalConfiguration().get("AZURE_TEXT_ANALYTICS_API_KEY")))
			.endpoint(Configuration.getGlobalConfiguration().get("AZURE_TEXT_ANALYTICS_ENDPOINT"))
			.buildClient();

	@RequestMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}

	@RequestMapping("/detectLanguage")
	public String detectLanguage() {
		return DetectLanguage.getSource(client);
	}

	@RequestMapping("/recognizeEntities")
	public String recognizeEntities() {
		return RecognizeEntities.getSource(client);
	}

	@RequestMapping("/recognizeLinkedEntities")
	public String recognizeLinkedEntities() {
		return RecognizeLinkedEntities.getSource(client);
	}

	@RequestMapping("/extractKeyPhrases")
	public String extractKeyPhrases() {
		return ExtractKeyPhrases.getSource(client);
	}

	@RequestMapping("/analyzeSentiment")
	public String analyzeSentiment() {
		return AnalyzeSentiment.getSource(client);
	}


	/*
		Batch String Operation
	 */

	@RequestMapping(value = "/detectLanguageBatchString", method = RequestMethod.GET)
	public String detectLanguageBatchString(@RequestParam List<String> documents,
		@RequestParam(value="isIncludeStats", required = false, defaultValue = "false") String isIncludeStats,
		@RequestParam(value="modelVersion", required = false, defaultValue = "latest") String modelVersion,
		@RequestParam(value="countryHint", required = false, defaultValue = "US") String countryHint) {

		return DetectLanguageBatchString.getSource(client, documents, isIncludeStats, modelVersion, countryHint);
	}

	@RequestMapping("/recognizeEntitiesBatchString")
	public String recognizeEntitiesBatchString(@RequestParam List<String> documents,
			@RequestParam(value="isIncludeStats", required = false, defaultValue = "false") String isIncludeStats,
			@RequestParam(value="modelVersion", required = false, defaultValue = "latest") String modelVersion,
			@RequestParam(value="languageCode", required = false, defaultValue = "en") String languageCode) {
		return RecognizeEntitiesBatchString.getSource(client, documents, isIncludeStats, modelVersion, languageCode);
	}

	@RequestMapping("/recognizeLinkedEntitiesBatchString")
	public String recognizeLinkedEntitiesBatchString(@RequestParam List<String> documents,
			@RequestParam(value="isIncludeStats", required = false, defaultValue = "false") String isIncludeStats,
			@RequestParam(value="modelVersion", required = false, defaultValue = "latest") String modelVersion,
			@RequestParam(value="languageCode", required = false, defaultValue = "en") String languageCode) {
		return RecognizeLinkedEntitiesBatchString.getSource(client, documents, isIncludeStats, modelVersion, languageCode);
	}

	@RequestMapping("/extractKeyPhrasesBatchString")
	public String extractKeyPhrasesBatchString(@RequestParam List<String> documents,
			@RequestParam(value="isIncludeStats", required = false, defaultValue = "false") String isIncludeStats,
			@RequestParam(value="modelVersion", required = false, defaultValue = "latest") String modelVersion,
			@RequestParam(value="languageCode", required = false, defaultValue = "en") String languageCode) {
		return ExtractKeyPhrasesBatchString.getSource(client, documents, isIncludeStats, modelVersion, languageCode);
	}

	@RequestMapping("/analyzeSentimentBatchString")
	public String analyzeSentimentBatchString(@RequestParam List<String> documents,
			@RequestParam(value="isIncludeStats", required = false, defaultValue = "false") String isIncludeStats,
			@RequestParam(value="modelVersion", required = false, defaultValue = "latest") String modelVersion,
			@RequestParam(value="languageCode", required = false, defaultValue = "en") String languageCode) {
		return AnalyzeSentimentBatchString.getSource(client, documents, isIncludeStats, modelVersion, languageCode);
	}


	/*
		Max overload Batch Operation
	 */

	@RequestMapping("/detectLanguageBatch")
	public String detectLanguageBatch(@RequestParam List<String> documents) {
		return DetectLanguageBatch.getSource(client, documents);
	}

	@RequestMapping("/recognizeEntitiesBatch")
	public String recognizeEntitiesBatch() {
		return RecognizeEntitiesBatch.getSource(client);
	}

	@RequestMapping("/recognizeLinkedEntitiesBatch")
	public String recognizeLinkedEntitiesBatch() {
		return RecognizeLinkedEntitiesBatch.getSource(client);
	}

	@RequestMapping("/extractKeyPhrasesBatch")
	public String extractKeyPhrasesBatch() {
		return ExtractKeyPhrasesBatch.getSource(client);
	}

	@RequestMapping("/analyzeSentimentBatch")
	public String analyzeSentimentBatch(@RequestParam List<String> documents) {
		return AnalyzeSentimentBatch.getSource(client);
	}
}
