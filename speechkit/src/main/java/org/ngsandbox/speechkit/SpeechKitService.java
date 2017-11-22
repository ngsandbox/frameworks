package org.ngsandbox.speechkit;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.ngsandbox.common.exceptions.HttpError;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;
import java.util.UUID;


/**
 * Example of service call:
 * POST /asr_xml?uuid=<UserIdentifier>&key=<ApiKey>&topic=queries HTTP/1.1
 * Host: asr.yandex.net
 * Content-Type: audio/x-wav
 */
@Slf4j
class SpeechKitService {

    private final String apiKey;
    private final String hostUrl;

    SpeechKitService(@NonNull String hostUrl, @NonNull String apiKey) {
        log.info("SpeechKit service initialization for host {} and api {}", hostUrl, apiKey);
        this.hostUrl = hostUrl;
        this.apiKey = apiKey;
    }

    InputStream post(@NonNull InputStream file) throws HttpError {
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        String uri = "/asr_xml?uuid=" + id + "&key=" + apiKey + "&topic=queries";
        try (CloseableHttpClient httpclient = HttpClientBuilder.create().build()) {
            HttpHost target = HttpHost.create(hostUrl);

            log.debug("Sending request {}", uri);
            HttpPost postRequest = new HttpPost(uri);
            postRequest.addHeader("Content-Type", "audio/x-wav");
            postRequest.setEntity(new InputStreamEntity(file, ContentType.DEFAULT_BINARY));

            HttpResponse httpResponse = httpclient.execute(target, postRequest);
            HttpEntity entity = httpResponse.getEntity();

            log.debug("Response status {} ", httpResponse.getStatusLine());
            Arrays.stream(httpResponse.getAllHeaders()).forEach(header -> log.debug("Response header {} ", header));
            return entity.getContent();
        } catch (Exception e) {
            log.error("Error to call host {} uri {} ", hostUrl, uri, e);
            throw new HttpError(hostUrl, uri, "Errror to call server", e);
        }
    }
}
