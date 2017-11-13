package org.ngsandbox.common.nlp;

import org.ngsandbox.common.nlp.entities.Part;
import org.ngsandbox.common.nlp.entities.Tag;

public interface NlpService {

    Part processSentence(String sentence);

    Tag findTag(Part part, String tag);

    String tagDescription(String tag);
}
