package org.ngsandbox.speechkit;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ngsandbox.common.file.StreamFileAdapter;
import org.ngsandbox.common.speech.SpeechService;
import org.ngsandbox.common.speech.entities.BaseResponse;
import org.ngsandbox.common.speech.entities.ResponseWrapper;
import org.ngsandbox.speechkit.dto.RecognitionResult;
import org.ngsandbox.speechkit.dto.Variant;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
class SpeechKitServiceTest {

    private InputStream propsStream;
    private InputStream sweetsWav;
    private Properties props;
    private SpeechService service;

    @BeforeEach
    void init() throws IOException {
        propsStream = SpeechKitServiceTest.class.getResourceAsStream("/application.properties");
        //wavStream = SpeechKitServiceTest.class.getResourceAsStream("/speech.wav");
        //wavStream = SpeechKitServiceTest.class.getResourceAsStream("/sber1.wav");
        sweetsWav = SpeechKitServiceTest.class.getResourceAsStream("/sweets.wav");
        Assertions.assertNotNull(propsStream, "application.properties file was not found");
        Assertions.assertNotNull(sweetsWav, "speech.wav was not found. Try run 'gradle :speechkit:downloadTest'");
        props = new Properties();
        props.load(propsStream);
        service = new SpeechKitServiceImpl(props);
    }

    @Test
    void testSpeechKitCall() throws IOException {
        ResponseWrapper<BaseResponse> wrapper = service.parseVoice(new StreamFileAdapter(sweetsWav, "swiits.wav"));
        log.trace("received result {}", wrapper);
        Assertions.assertNotNull(wrapper);
        Assertions.assertNotNull(wrapper.getStatus());
        Assertions.assertTrue(wrapper.getStatus().isSuccess());
        Assertions.assertNotNull(wrapper.getResponse());
        Assertions.assertEquals(wrapper.getResponse().getText(), "где мои конфетки");
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

        if (sweetsWav != null) {
            sweetsWav.close();
            sweetsWav = null;
        }
    }
}
