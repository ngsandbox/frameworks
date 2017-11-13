package org.ngsandbox.common.nlp;

import org.ngsandbox.common.nlp.entities.Part;

public interface NlpService {

    Part processSentence(String sentence);

    String tagDescription(String tag);
}
