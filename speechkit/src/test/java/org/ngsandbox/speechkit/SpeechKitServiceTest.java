package org.ngsandbox.speechkit;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

@Slf4j
class SpeechKitServiceTest {

    @Test
    void testSpeechKitCall() throws IOException {
        try (InputStream propsStream = SpeechKitServiceTest.class.getResourceAsStream("/application.properties")) {
            Objects.requireNonNull(propsStream, "application.properties file was not found");
            Properties props = new Properties();
            props.load(propsStream);
            try (InputStream wavStream = SpeechKitServiceTest.class.getResourceAsStream("/speech.wav")) {
                Objects.requireNonNull(wavStream, "speech.wav was not found. Try run 'gradle :speechkit:downloadTest'");
                SpeechKitService service = new SpeechKitService(props);
                String result = service.post(wavStream);
                Assertions.assertNotNull(result);
                log.trace("received result {}", result);
            }
        }
    }
}
