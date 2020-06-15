/**
 *
 * App
 *
 * This component is the skeleton around the actual pages, and should only
 * contain code that should be seen on all pages. (e.g. navigation bar)
 */

import React from 'react';
import { Helmet } from 'react-helmet';
import styled from 'styled-components';
import { Switch, Route } from 'react-router-dom';

import HomePage from 'containers/HomePage/Loadable';
import NotFoundPage from 'containers/NotFoundPage/Loadable';
import LanguageDetection from 'containers/LanguageDetection/Loadable';
import SentimentAnalysis from 'containers/SentimentAnalysis/Loadable';
import KeyPhrasesExtraction from 'containers/KeyPhrasesExtraction/Loadable';
import EntitiesRecognition from 'containers/EntitiesRecognition/Loadable';
import LinkedEntitiesRecognition from 'containers/LinkedEntitiesRecognition/Loadable';

import Header from 'components/Header';

import GlobalStyle from '../../global-styles';

const AppWrapper = styled.div`
  max-width: calc(768px + 16px * 2);
  margin: 0 auto;
  display: flex;
  min-height: 100%;
  padding: 0 16px;
  flex-direction: column;
`;

export default function App() {
  return (
    <AppWrapper>
      <Helmet
        titleTemplate="%s - React.js Boilerplate"
        defaultTitle="Text Analytics Java SDK"
      >
        <meta name="description" content="A React.js Boilerplate application" />
      </Helmet>
      <Header />
      <Switch>
        <Route exact path="/" component={HomePage} />
        <Route path="/languageDetection" component={LanguageDetection} />
        <Route path="/sentimentAnalysis" component={SentimentAnalysis} />
        <Route path="/keyPhrasesExtraction" component={KeyPhrasesExtraction} />
        <Route path="/entitiesRecognition" component={EntitiesRecognition} />
        <Route path="/linkedEntitiesRecognition" component={LinkedEntitiesRecognition} />
        <Route path="" component={NotFoundPage} />
      </Switch>
      <GlobalStyle />
    </AppWrapper>
  );
}
