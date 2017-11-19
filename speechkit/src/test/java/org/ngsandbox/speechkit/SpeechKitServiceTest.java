package org.ngsandbox.speechkit;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ngsandbox.common.speech.RecognitionResult;
import org.ngsandbox.common.speech.Variant;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
class SpeechKitServiceTest {

    private InputStream propsStream;
    private InputStream wavStream;
    private Properties props;

    @BeforeEach
    void init() throws IOException {
        propsStream = SpeechKitServiceTest.class.getResourceAsStream("/application.properties");
        //wavStream = SpeechKitServiceTest.class.getResourceAsStream("/speech.wav");
        //wavStream = SpeechKitServiceTest.class.getResourceAsStream("/sber1.wav");
        wavStream = SpeechKitServiceTest.class.getResourceAsStream("/sweets.wav");
        Assertions.assertNotNull(propsStream, "application.properties file was not found");
        Assertions.assertNotNull(wavStream, "speech.wav was not found. Try run 'gradle :speechkit:downloadTest'");
        props = new Properties();
        props.load(propsStream);
    }

    @Test
    void testSpeechKitCall() throws IOException {
        SpeechKitService service = new SpeechKitService(props);
        String result = service.post(wavStream);
        log.trace("received result {}", result);
        Assertions.assertNotNull(result);
    }


    @Test
    void testSKResponse() throws Exception {
        try (InputStream resp = SpeechKitServiceTest.class.getResourceAsStream("/yaSpeechKitResponse.xml")) {
            ResponseConverter responseConverter = new ResponseConverter(resp);
            RecognitionResult recognitionResult = responseConverter.getRecognitionResult();
            log.debug("Result recognition {}", recognitionResult);
            Assertions.assertNotNull(recognitionResult);
            Assertions.assertEquals(recognitionResult.getSuccess(), 1);
            Assertions.assertFalse(recognitionResult.getVariants().isEmpty());
            Variant variant = recognitionResult.getVariants().get(0);
            Assertions.assertEquals(variant.getConfidence(), 1, 0.001);
            Assertions.assertEquals(variant.getValue(), "твой номер 212-85-06");
        }
    }



    @AfterEach
    void close() throws IOException {
        if (propsStream != null) {
            propsStream.close();
            propsStream = null;
        }

        if (wavStream != null) {
            wavStream.close();
            wavStream = null;
        }
    }
}
