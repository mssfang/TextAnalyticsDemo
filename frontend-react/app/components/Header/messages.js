/*
 * HomePage Messages
 *
 * This contains all the text for the HomePage component.
 */
import { defineMessages } from 'react-intl';

export const scope = 'boilerplate.components.Header';

export default defineMessages({
  home: {
    id: `${scope}.home`,
    defaultMessage: 'Home',
  },
  features: {
    id: `${scope}.features`,
    defaultMessage: 'Features',
  },
  languageDetection: {
    id: `${scope}.languageDetection`,
    defaultMessage: 'Language Detection',
  },
  sentimentAnalysis: {
    id: `${scope}.sentimentAnalysis`,
    defaultMessage: 'Sentiment Analysis',
  },
  keyPhrasesExtraction: {
    id: `${scope}.keyPhrasesExtraction`,
    defaultMessage: 'Key Phrases Extraction',
  },
  entitiesRecognition: {
    id: `${scope}.entitiesRecognition`,
    defaultMessage: 'Entities Recognition',
  },
  linkedEntitiesRecognition: {
    id: `${scope}.linkedEntitiesRecognition`,
    defaultMessage: 'Linked Entities Recognition',
  },
});
