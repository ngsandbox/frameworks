package org.ngsandbox.face;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.ngsandbox.common.face.RegRequest;
import org.ngsandbox.common.face.RegResponse;
import org.ngsandbox.common.file.Base64FileAdapter;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class FaceServiceTest {

    @Test
    public void testPostRequest() throws Exception {
        String hostname = "http://93.88.76.57:8090";
        String uri = "/regist";
        String fileName = "face.jpg";
        try (InputStream inputStream = FaceServiceTest.class.getResourceAsStream("/" + fileName)) {
            FaceServiceImpl<RegRequest, RegResponse> serv = new FaceServiceImpl<>(RegRequest.class, RegResponse.class);
            RegRequest req = RegRequest.builder()
                    .fotoName(fileName)
                    .desc("test photo")
                    .imageStr(new Base64FileAdapter(fileName, IOUtils.toString(inputStream, StandardCharsets.UTF_8)).getContent())
                    .login("vma-test")
                    .build();
            RegResponse regResponse = serv.postContent(hostname, uri, req);
            Assertions.assertNotNull(regResponse);
        }
    }
}
