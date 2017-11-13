package org.ngsandbox.nlp;

import lombok.extern.slf4j.Slf4j;
import org.ngsandbox.common.nlp.entities.Part;
import org.junit.jupiter.api.Test;

@Slf4j
public class NlpServiceImplTest {

    @Test
    public void testProcessSentence() {
        NlpServiceImpl nlpService = new NlpServiceImpl();
        Part part = nlpService.processSentence("hi");
        log.debug("Result {}", part);
    }
}
