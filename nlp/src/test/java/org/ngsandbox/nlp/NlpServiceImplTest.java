package org.ngsandbox.nlp;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.ngsandbox.common.nlp.TagHelper;
import org.ngsandbox.common.nlp.entities.Part;
import org.ngsandbox.common.nlp.entities.Tag;

@Slf4j
public class NlpServiceImplTest {

    @Test
    public void testSayHiSentence() {
        NlpServiceImpl nlpService = new NlpServiceImpl();
        testGreeting(nlpService, "hi");
        testGreeting(nlpService, "Hello");
        testGreeting(nlpService, "Hello Robot!");
        testGreeting(nlpService, "Hey man!");
        testGreeting(nlpService, "Good morning!");
        testGreeting(nlpService, "Good afternoon!");
        testGreeting(nlpService, "Good evening!");
        testGreeting(nlpService, "Please!");
        //testGreeting(nlpService, "Nice to see you!");
    }

    @Test
    public void testSayHelloSentence() {
        NlpServiceImpl nlpService = new NlpServiceImpl();
        Part part = nlpService.processSentence("Hello");
        log.debug("Result {}", part);
        Assertions.assertNotNull(part);
        Assertions.assertTrue(part instanceof Tag);
        Tag root = (Tag) part;

        Assertions.assertEquals(root.getTag(), TagHelper.ROOT);
        Assertions.assertFalse(root.getChildren().isEmpty());

        Tag intj = (Tag) root.getChildren().get(0);
        Assertions.assertEquals(intj.getTag(), TagHelper.INTJ);
        Assertions.assertFalse(intj.getChildren().isEmpty());
    }

    private void testGreeting(NlpServiceImpl nlpService, String greeting) {
        Part part = nlpService.processSentence(greeting);
        log.debug("Result {}", part);
        Tag intj = nlpService.findTag(part, TagHelper.INTJ);
        Assertions.assertNotNull(intj, "INTJ tag not found for sentence " + greeting);
//        Tag uh = nlpService.findTag(intj, TagHelper.UH);
//        Assertions.assertNotNull(uh, "UH tag not found for sentence " + greeting);
    }

}
