package org.ngsandbox.face;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.ngsandbox.common.exceptions.HttpError;

import java.io.InputStream;
import java.util.Arrays;


@Slf4j
class RemoteFaceService<B> {

    private final ObjectMapper mapper = new ObjectMapper();
    private final Class<B> bodyClazz;
    private final String hostUrl;

    RemoteFaceService(@NonNull String hostUrl, @NonNull Class<B> resClazz) {
        this.bodyClazz = resClazz;
        this.hostUrl = hostUrl;
    }

    B post(String uri, Object content) throws HttpError {
        try (CloseableHttpClient httpclient = HttpClientBuilder.create().build()) {
            HttpHost target = HttpHost.create(hostUrl);

            HttpPost postRequest = new HttpPost(uri);
            postRequest.addHeader("Content-Type", "application/json");
            postRequest.addHeader("Accept", "application/json");
            postRequest.addHeader("Accept-Encoding", "gzip, deflate");
            postRequest.addHeader("Connection", "Keep-Alive");
            String json = mapper.writeValueAsString(content);
            postRequest.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));

            HttpResponse httpResponse = httpclient.execute(target, postRequest);
            HttpEntity entity = httpResponse.getEntity();
            log.debug("Response status {} ", httpResponse.getStatusLine());
            Arrays.stream(httpResponse.getAllHeaders()).forEach(header -> log.debug("Response header {} ", header));

            if (httpResponse.getStatusLine().getStatusCode() != 200) {
                String err = httpResponse.getStatusLine().toString();
                if (entity != null) {
                    try (InputStream in = entity.getContent()) {
                        err += IOUtils.toString(in, "UTF-8");
                    }
                }
                throw new HttpError(hostUrl, uri, err);
            }

            //log.debug("Response entity {} ", entity != null ? EntityUtils.toString(entity) : "");
            return entity != null ? mapper.readValue(entity.getContent(), bodyClazz) : null;
        } catch (Exception e) {
            log.error("Error to call host {} uri {} ", hostUrl, uri, e);
            throw new HttpError(hostUrl, uri, "Errror to call server", e);
        }
    }
}
