package org.frameworks.common.nlp;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Helper {

    public final static Map<String, String> TAGS;

    static {
        Map<String, String> tags = new HashMap<>();
        tags.put("*", "\"Understood\" subject of infinitive or imperative");
        tags.put("0", "Zero variant of that in subordinate clauses ");
        tags.put("ADJP", "Adjective phrase ");
        tags.put("ADVP", "Adverb phrase ");
        tags.put("CC", "Coordinating conjunction ");
        tags.put("CD", "Cardinal number ");
        tags.put("DT", "Determiner ");
        tags.put("EX", "Existential there ");
        tags.put("FW", "Foreign word ");
        tags.put("IN", "Preposition or subordinating conjunction ");
        tags.put("INTJ", "Interjection. Corresponds approximately to the part-of-speech tag UH.");
        tags.put("JJ", "Adjective ");
        tags.put("JJR", "Adjective, comparative ");
        tags.put("JJS", "Adjective, superlative ");
        tags.put("LS", "List item marker ");
        tags.put("MD", "Modal ");
        tags.put("NN", "Noun, singular or mass ");
        tags.put("NNP", "Proper noun, singular ");
        tags.put("NNPS", "Proper noun, plural ");
        tags.put("NNS", "Noun, plural ");
        tags.put("NP", "Noun phrase ");
        tags.put("PDT", "Predeterminer ");
        tags.put("POS", "Possessive ending ");
        tags.put("PP", "Prepositional phrase ");
        tags.put("PRP", "Personal pronoun ");
        tags.put("PRP$", "Possessive pronoun ");
        tags.put("RB", "Adverb ");
        tags.put("RBR", "Adverb, comparative ");
        tags.put("RBS", "Adverb, superlative ");
        tags.put("RP", "Particle ");
        tags.put("SBARQ", "Direct question introduced by wh-element ");
        tags.put("SBAR", "Subordinate clause ");
        tags.put("SINV", "Declarative sentence with subject-aux inversion ");
        tags.put("SQ", "Yes/no questions and subconstituent of SBARQ excluding wh-element ");
        tags.put("S", "Simple declarative clause ");
        tags.put("SYM", "Symbol ");
        tags.put("TO", "to ");
        tags.put("T", "Trace of wh-Constituent ");
        tags.put("UH", "Interjection ");
        tags.put("VBD", "Verb, past tense ");
        tags.put("VBG", "Verb, gerund or present participle ");
        tags.put("VBN", "Verb, past participle ");
        tags.put("VBP", "Verb, non-3rd person singular present ");
        tags.put("VB", "Verb, base form ");
        tags.put("VBZ", "Verb, 3rd person singular present ");
        tags.put("VP", "Verb phrase ");
        tags.put("WDT", "Wh-determiner ");
        tags.put("WHADVP", "Wh-adverb phrase ");
        tags.put("WHNP", "Wh-noun phrase ");
        tags.put("WHPP", "Wh-prepositional phrase ");
        tags.put("WP$", "Possessive wh-pronoun ");
        tags.put("WP", "Wh-pronoun ");
        tags.put("WRB", "Wh-adverb ");
        tags.put("X", "Constituent of unknown or uncertain category ");
        TAGS = Collections.unmodifiableMap(tags);
    }
}
