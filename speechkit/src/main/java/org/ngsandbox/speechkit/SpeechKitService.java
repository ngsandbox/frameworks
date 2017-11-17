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


@Slf4j
class SpeechKitService {

    private final String apiKey;
    private final String hostUrl;

    /*
    POST /asr_xml?uuid=<идентификатор пользователя>&key=<API-ключ>&topic=queries HTTP/1.1
Host: asr.yandex.net
Content-Type: audio/x-wav
    * */

    SpeechKitService(@NonNull Properties props) {
        String host = props.getProperty("host");
        hostUrl = host == null ? "https://asr.yandex.net" : host;
        this.apiKey = props.getProperty("apiKey");
        Objects.requireNonNull(this.apiKey, "Api key for SpeechKit is not specified");
    }

    String post(@NonNull InputStream file) throws HttpError {
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
            String body = null;
            if (entity != null) {
                try (InputStreamReader inputStreamReader = new InputStreamReader(entity.getContent())) {
                    body = IOUtils.toString(inputStreamReader);
                    log.trace("Receiver content {}", body);
                }
            } else {
                log.error("Nothing has been received from uri {}", uri);
            }

            return body;
        } catch (Exception e) {
            log.error("Error to call host {} uri {} ", hostUrl, uri, e);
            throw new HttpError(hostUrl, uri, "Errror to call server", e);
        }
    }
}
