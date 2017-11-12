package org.frameworks.common.nlp;

import org.frameworks.common.nlp.entities.Part;

public interface NlpService {

    Part processSentence(String sentence);

    String tagDescription(String tag);
}
