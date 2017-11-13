package org.ngsandbox.nlp;

import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.trees.Tree;
import lombok.extern.slf4j.Slf4j;
import org.ngsandbox.common.nlp.Helper;
import org.ngsandbox.common.nlp.NlpService;
import org.ngsandbox.common.nlp.entities.Part;
import org.ngsandbox.common.nlp.entities.Tag;
import org.ngsandbox.common.nlp.entities.Word;

import java.util.List;

@Slf4j
public class NlpServiceImpl implements NlpService {

    public NlpServiceImpl() {
    }

    @Override
    public Part processSentence(String sentence) {
        log.debug("Start process sentence {}", sentence);
        // read some text in the text variable
        String text = sentence == null ? "Where can I find the closest Sberbank's ATM?" : sentence;// Add your text here!
        String textRu = "Где найти ближайшее отделение Сбербанка?";// Add your text here!

        Sentence sent = new Sentence(text);
        Part part = convert(sent.parse());
        log.debug("Result processed sentence {}", part);
        List<String> nerTags = sent.nerTags();  // [PERSON, O, O, O, O, O, O, O]
        log.debug("Tags: {}", nerTags);
        return part;
    }

    public static Part convert(Tree tree) {
        if (tree == null) {
            return null;
        }

        Part part = null;
        log.debug("Tree: {}", tree);
        if (tree.label() != null) {
            if (Double.isNaN(tree.score())) {
                part = new Word(tree.label().value());
            } else {
                String tag = tree.label().value();
                part = Tag.builder()
                        .tag(tag)
                        .label(Helper.TAGS.get(tag))
                        .build();
            }
        }

        if (!tree.isLeaf() && tree.children() != null && tree.children().length > 0) {
            Tag.TagBuilder builder = Tag.builder();
            if (part != null && part instanceof Tag) {
                builder.label(((Tag) part).getLabel());
                builder.tag(((Tag) part).getTag());
            } else {
                log.error("tag is null of word but has leafs {}", part);
            }

            for (Tree kid : tree.children()) {
                builder.child(convert(kid));
            }

            part = builder.build();
        }

        return part;
    }

    @Override
    public String tagDescription(String tag) {
        return Helper.TAGS.get(tag);
    }
}
