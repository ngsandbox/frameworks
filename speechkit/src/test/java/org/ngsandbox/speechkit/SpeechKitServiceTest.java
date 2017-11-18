package org.ngsandbox.speechkit;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
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
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse((File) null);

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
