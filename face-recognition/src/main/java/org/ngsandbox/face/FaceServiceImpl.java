package org.ngsandbox.face;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.ngsandbox.common.exceptions.HttpError;

import java.util.Arrays;


@Slf4j
public class FaceServiceImpl<R, B> {

    private final ObjectMapper mapper = new ObjectMapper();
    private final Class<R> reqClazz;
    private final Class<B> bodyClazz;

    public FaceServiceImpl(@NonNull Class<R> reqClazz, @NonNull Class<B> resClazz) {
        this.reqClazz = reqClazz;
        this.bodyClazz = resClazz;
    }

    public B postContent(String hostUrl, String uri, R reqBody) {
        try (CloseableHttpClient httpclient = HttpClientBuilder.create().build()) {
            HttpHost target = HttpHost.create(hostUrl);

            HttpPost postRequest = new HttpPost(uri);
            /*
            "Content-Type", "application/json"
"Accept-Encoding", "gzip, deflate"
"Accept", "application/json"
"Connection", "Keep-Alive"

            */
            postRequest.addHeader("Content-Type", "application/json");
            postRequest.addHeader("Accept", "application/json");
            postRequest.setEntity(new ByteArrayEntity(mapper.writeValueAsBytes(reqBody)));

            HttpResponse httpResponse = httpclient.execute(target, postRequest);
            HttpEntity entity = httpResponse.getEntity();


            log.debug("Response status {} ", httpResponse.getStatusLine());
            Arrays.stream(httpResponse.getAllHeaders()).forEach(header -> log.debug("Response header {} ", header));

            //log.debug("Response entity {} ", entity != null ? EntityUtils.toString(entity) : "");
            return entity != null ? mapper.readValue(entity.getContent(), bodyClazz) : null;
        } catch (Exception e) {
            log.error("Error to call host {} uri {} ", hostUrl, uri, e);
            throw new HttpError(hostUrl, uri, "Errror to call server", e);
        }
    }
}
