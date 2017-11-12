package org.ngsandbox.snlp;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.util.Quadruple;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.Properties;

@Slf4j
public class StanfordProgram {
    public static void main(String... args) throws Exception {
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // read some text in the text variable
        String text = "Where can I find the closest Sberbank's ATM?";// Add your text here!
        String textRu = "Где найти ближайшее отделение Сбербанка?";// Add your text here!

        Sentence sent = new Sentence(text);
        List<String> nerTags = sent.nerTags();  // [PERSON, O, O, O, O, O, O, O]
        String posTag = sent.posTag(0);   // NNP
        String nerTag = sent.nerTag(0);   // NNP
        Collection<Quadruple<String, String, String, Double>> kbp = sent.kbp();
        log.debug("Tags: {}", nerTags);
        log.debug("Kbp: {}", kbp);
        log.debug("posTag: {}; nerTag: {};", posTag, nerTag);
        log.debug("Tree: {}", sent.parse());


    }
}
