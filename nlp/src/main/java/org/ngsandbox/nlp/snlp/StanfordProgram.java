package org.ngsandbox.nlp.snlp;

import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.Quadruple;
import lombok.extern.slf4j.Slf4j;
import org.ngsandbox.common.nlp.entities.Part;
import org.ngsandbox.nlp.NlpServiceImpl;

import java.util.Collection;
import java.util.List;

@Slf4j
public class StanfordProgram {
    public static void main(String... args) throws Exception {
//        Properties props = new Properties();
//        props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
//        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // read some text in the text variable
        String text = "Where can I find the closest Sberbank's ATM?";// Add your text here!
        String textRu = "Где найти ближайшее отделение Сбербанка?";// Add your text here!

        Sentence sent = new Sentence(text);
        Tree tree = sent.parse();
        Part part = NlpServiceImpl.convert(tree);
        log.debug("Tree: {}", tree);
        log.debug("Words tree: {}", part);

        List<String> nerTags = sent.nerTags();  // [PERSON, O, O, O, O, O, O, O]
        log.debug("Tags: {}", nerTags);
        String posTag = sent.posTag(0);   // NNP
        String nerTag = sent.nerTag(0);   // NNP
        log.debug("posTag: {}; nerTag: {};", posTag, nerTag);
        Collection<Quadruple<String, String, String, Double>> kbp = sent.kbp();
        log.debug("Kbp: {}", kbp);


    }
}
