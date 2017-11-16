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
import java.util.UUID;

@Slf4j
class FaceServiceTest {
    private static final String HOST_NAME = "http://93.88.76.57:8090";

    @Test
    void testRegisterRequest() throws Exception {
        String fileName = "face.jpg";
        try (InputStream inputStream = FaceServiceTest.class.getResourceAsStream("/" + fileName)) {
            RemoteFaceServiceImpl serv = new RemoteFaceServiceImpl(HOST_NAME);
            ResponseWrapper<RegResponse> regResponse = serv.register(UUID.randomUUID().toString(), new Base64FileAdapter(fileName, inputStream));
            Assertions.assertNotNull(regResponse.getResponse());
            Assertions.assertEquals(regResponse.getStatus(), ResponseStatus.OK);
            Assertions.assertEquals(regResponse.getResponse().getStatus(), "SUCCESS");
        }
    }

    @Test
    void testAuthRequest() throws Exception {
        String fileName = "face.jpg";
        try (InputStream inputStream = FaceServiceTest.class.getResourceAsStream("/" + fileName)) {
            RemoteFaceServiceImpl serv = new RemoteFaceServiceImpl(HOST_NAME);
            ResponseWrapper<AuthResponse> regResponse = serv.auth(UUID.randomUUID().toString(), new Base64FileAdapter(fileName, inputStream));
            Assertions.assertNotNull(regResponse);
            Assertions.assertNotNull(regResponse.getResponse());
            Assertions.assertEquals(regResponse.getStatus(), ResponseStatus.OK);
            Assertions.assertEquals(regResponse.getResponse().getStatus(), "ERROR");
        }
    }

    @Test
    void testFindRequest() throws Exception {
        String fileName = "face.jpg";
        try (InputStream inputStream = FaceServiceTest.class.getResourceAsStream("/" + fileName)) {
            RemoteFaceServiceImpl serv = new RemoteFaceServiceImpl(HOST_NAME);
            ResponseWrapper<FindResponse> regResponse = serv.find(new Base64FileAdapter(fileName, inputStream));
            Assertions.assertNotNull(regResponse.getResponse());
            Assertions.assertEquals(regResponse.getStatus(), ResponseStatus.OK);
            Assertions.assertEquals(regResponse.getResponse().getStatus(), "SUCCESS");
        }
    }
}
