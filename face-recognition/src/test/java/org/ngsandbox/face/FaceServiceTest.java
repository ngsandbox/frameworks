package org.ngsandbox.face;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.ngsandbox.common.face.ResponseStatus;
import org.ngsandbox.common.face.ResponseWrapper;
import org.ngsandbox.common.face.dto.AuthResponse;
import org.ngsandbox.common.face.dto.FindResponse;
import org.ngsandbox.common.face.dto.RegResponse;
import org.ngsandbox.common.file.Base64FileAdapter;

import java.io.InputStream;

@Slf4j
class FaceServiceTest {
    private static final String HOST_NAME = "http://93.88.76.57:8090";
    private static final String CAT_FILE = "cat.jpg";
    private static final String HUMAN_FILE = "face.jpg";
    private static final String HUMAN_LOGIN = "Negan";

    @Test
    void testRegisterRequest() throws Exception {
        try (InputStream inputStream = getResource(HUMAN_FILE)) {
            RemoteFaceServiceImpl serv = new RemoteFaceServiceImpl(HOST_NAME);
            ResponseWrapper<RegResponse> regResponse = serv.register(HUMAN_LOGIN, new Base64FileAdapter(HUMAN_FILE, inputStream));
            Assertions.assertNotNull(regResponse.getResponse());
            Assertions.assertEquals(regResponse.getStatus(), ResponseStatus.OK);
            Assertions.assertEquals(regResponse.getResponse().getStatus(), "SUCCESS");
        }
    }

    @Test
    void testAuthRequest() throws Exception {
        try (InputStream inputStream = getResource(HUMAN_FILE)) {
            RemoteFaceServiceImpl serv = new RemoteFaceServiceImpl(HOST_NAME);
            ResponseWrapper<AuthResponse> regResponse = serv.auth(HUMAN_LOGIN, new Base64FileAdapter(HUMAN_FILE, inputStream));
            Assertions.assertNotNull(regResponse);
            Assertions.assertNotNull(regResponse.getResponse());
            Assertions.assertEquals(regResponse.getStatus(), ResponseStatus.OK);
            Assertions.assertNotEquals(regResponse.getResponse().getStatus(), "ERROR");
        }
    }


    @Test
    void testFindRequest() throws Exception {
        try (InputStream inputStream = getResource(HUMAN_FILE)) {
            RemoteFaceServiceImpl serv = new RemoteFaceServiceImpl(HOST_NAME);
            ResponseWrapper<FindResponse> regResponse = serv.find(new Base64FileAdapter(HUMAN_FILE, inputStream));
            Assertions.assertNotNull(regResponse.getResponse());
            Assertions.assertEquals(regResponse.getStatus(), ResponseStatus.OK);
            Assertions.assertNotNull(regResponse.getResponse().getLogin());
            Assertions.assertEquals(regResponse.getResponse().getLogin(), HUMAN_LOGIN);
            Assertions.assertEquals(regResponse.getResponse().getStatus(), "SUCCESS");
        }
    }

    @Test
    void testNonFoundRequest() throws Exception {
        try (InputStream inputStream = getResource(CAT_FILE)) {
            RemoteFaceServiceImpl serv = new RemoteFaceServiceImpl(HOST_NAME);
            ResponseWrapper<FindResponse> regResponse = serv.find(new Base64FileAdapter(CAT_FILE, inputStream));
            Assertions.assertNull(regResponse.getResponse());
            Assertions.assertNotEquals(regResponse.getStatus(), ResponseStatus.OK);
        }
    }

    private InputStream getResource(String fileName) {
        InputStream inputStream = FaceServiceTest.class.getResourceAsStream("/" + fileName);
        Assertions.assertNotNull(inputStream);
        return inputStream;
    }
}
