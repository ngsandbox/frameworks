package org.ngsandbox.nlp.snlp;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.process.PTBTokenizer;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;

@Slf4j
public class TrainTokens implements Closeable {

    private final static String TRAINING_SET_IN = "/train-test.in";
    private final static String TOKENS_OUT = "/train-test.tok";
    private final static String CRF_PROPS = "/train-test.tok";
    private final InputStream isTrainingSet;
    private final InputStream isTokens;
    private final InputStream isCrfProps;

    public TrainTokens() {
        isTrainingSet = StanfordProgram.class.getResourceAsStream(TRAINING_SET_IN);
        isTokens = StanfordProgram.class.getResourceAsStream(TOKENS_OUT);
        isCrfProps = StanfordProgram.class.getResourceAsStream(CRF_PROPS);
    }

    public void run() throws Exception {
        PTBTokenizer tokenizer = PTBTokenizer.newPTBTokenizer(new InputStreamReader(isTrainingSet));
        tokenizer.tokenize().forEach(t -> log.debug("Token {}", t));

        Properties props = new Properties();
        props.load(isCrfProps);
        CRFClassifier crfClassifier = new CRFClassifier(props);
        crfClassifier.serializeClassifier("./output.txt.gz");
    }

    @Override
    public void close() throws IOException {
        if (isTrainingSet != null) {
            isTrainingSet.close();
        }

        if (isTokens != null) {
            isTokens.close();
        }

        if (isCrfProps != null) {
            isCrfProps.close();
        }
    }
}
