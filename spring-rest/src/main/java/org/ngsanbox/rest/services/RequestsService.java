package org.ngsanbox.rest.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.ngsanbox.rest.adapters.FileAdapter;
import org.ngsanbox.rest.entities.RequestInfo;
import org.ngsanbox.rest.entities.RequestStatus;
import org.ngsanbox.rest.exceptions.FileProcessError;
import org.ngsanbox.rest.exceptions.RequestNotFound;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class RequestsService {

    private static final Map<String, RequestInfo> requests = new ConcurrentHashMap<>();

    public List<String> getRequestsIds() {
        return new ArrayList<>(requests.keySet());
    }

    public RequestInfo getRequestInfo(String id) {
        return requests.get(id);
    }

    private RequestInfo handleRequestInfo(String id) {
        if (id == null) {
            id = UUID.randomUUID().toString();
            log.trace("Create new request info for id {}", id);
            RequestInfo requestInfo = RequestInfo.builder()
                    .id(id)
                    .status(RequestStatus.Initialized).build();
            requests.put(requestInfo.getId(), requestInfo);
            log.trace("Request info for id {} created. {}", id, requestInfo);
            return requestInfo;
        } else {
            log.trace("Receive request info for id {}", id);
            return getRequestInfo(id);
        }
    }

    public String saveFile(String id, @NotNull FileAdapter fileAdapter) {
        log.debug("Saving file content {} for request id {}", id, fileAdapter.getFilename());
        RequestInfo requestInfo = handleRequestInfo(id);
        requestInfo.setFileName(fileAdapter.getFilename());
        requestInfo.setFileBody(fileAdapter.getContent());
        return requestInfo.getId();
    }

    private InputStream getFileStream(String id) {
        log.debug("Try to get file stream by id {}", id);
        RequestInfo info = getRequestInfo(id);
        if (info == null) {
            throw new RequestNotFound("Request not found by id: " + id);
        }

        return new ByteArrayInputStream(info.getFileBody());

    }

    public void outputImageStream(String id, HttpServletResponse response) {
        log.debug("Return image sream for request id {}", id);
        try (InputStream stream = getFileStream(id)) {
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            IOUtils.copy(stream, response.getOutputStream());
        } catch (IOException ex) {
            log.error("Error to process file stram for request id {}", id, ex);
            throw new FileProcessError("Error to output file stream by id " + id, ex);
        }
    }

}
