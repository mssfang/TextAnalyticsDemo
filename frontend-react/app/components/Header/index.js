import React from 'react';
import { FormattedMessage } from 'react-intl';

import A from './A';
import Img from './Img';
import NavBar from './NavBar';
import HeaderLink from './HeaderLink';
import Banner from './azure-icon.png';
import messages from './messages';

function Header() {
  return (
    <div>
      <NavBar>
        <HeaderLink to="/languageDetection">
          <FormattedMessage {...messages.languageDetection} />
        </HeaderLink>
        <HeaderLink to="/keyPhrasesExtraction">
          <FormattedMessage {...messages.keyPhrasesExtraction} />
        </HeaderLink>
        <HeaderLink to="/sentimentAnalysis">
          <FormattedMessage {...messages.sentimentAnalysis} />
        </HeaderLink>
        <HeaderLink to="/entitiesRecognition">
          <FormattedMessage {...messages.entitiesRecognition} />
        </HeaderLink>
        <HeaderLink to="/linkedEntitiesRecognition">
          <FormattedMessage {...messages.linkedEntitiesRecognition} />
        </HeaderLink>
      </NavBar>
    </div>
  );
}

export default Header;
