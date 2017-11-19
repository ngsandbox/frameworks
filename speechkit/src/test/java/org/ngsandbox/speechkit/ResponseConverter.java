package org.ngsandbox.speechkit;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.ngsandbox.common.exceptions.FileProcessError;
import org.ngsandbox.common.speech.RecognitionResult;
import org.ngsandbox.common.speech.Variant;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

@Slf4j
public class ResponseConverter {

    private final InputStream stream;

    public ResponseConverter(@NonNull InputStream stream) {
        this.stream = stream;
    }

    public RecognitionResult getRecognitionResult() {
        try {
            RecognitionResult.RecognitionResultBuilder builder = RecognitionResult.builder();
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(stream);
            if (doc.getChildNodes() != null && doc.getChildNodes().getLength() > 0) {
                Node main = doc.getChildNodes().item(0);
                if ("recognitionResults".equals(main.getNodeName())) {
                    Node success = main.getAttributes().getNamedItem("success");
                    if (success != null) {
                        builder.success(Integer.parseInt(success.getNodeValue()));
                    } else {
                        log.error("Infomation about success was not found");
                    }
                    //Assertions.assertNotNull(success);
                } else {
                    log.warn("Skipped node {}", main.getNodeName());
                }


                if (main.getChildNodes() != null && main.getChildNodes().getLength() > 0) {
                    for (int count = 0; count < main.getChildNodes().getLength(); count++) {
                        Node varNode = main.getChildNodes().item(count);
                        if ("variant".equals(varNode.getNodeName())) {
                            Node confidenceAttr = varNode.getAttributes().getNamedItem("confidence");
                            if (confidenceAttr != null) {
                                String confidence = confidenceAttr.getNodeValue();
                                builder.variant(new Variant(Double.parseDouble(confidence), varNode.getTextContent()));
                            } else {
                                log.error("The confidence attribute was not found in variant node", varNode);
                            }
                        } else {
                            log.trace("Skipped node {}", main.getNodeName());
                        }
                    }
                } else {
                    log.error("The list of child recognition nodes is empty");
                }
            } else {
                log.error("The xml response is empty {}", doc.getTextContent());
            }

            //Assertions.assertEquals(main.getNodeName(), "recognitionResults");

            return builder.build();
        } catch (Exception ex) {
            log.error("Error to get xml from input stream ", ex);
            throw new FileProcessError("Error to get process result from speech recognition response. " + ex.getMessage());
        }
    }
}
