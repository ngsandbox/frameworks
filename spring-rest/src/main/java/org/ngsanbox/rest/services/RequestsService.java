package org.ngsanbox.rest.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.frameworks.common.nao.RequestsDao;
import org.frameworks.common.nao.entities.FileInfo;
import org.frameworks.common.nao.entities.RequestInfo;
import org.frameworks.common.nao.entities.RequestStatus;
import org.ngsanbox.rest.adapters.FileAdapter;
import org.ngsanbox.rest.exceptions.FileProcessError;
import org.ngsanbox.rest.exceptions.RequestNotFound;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final RequestsDao requestsDao;

    @Autowired
    public RequestsService(RequestsDao requestsDao) {
        this.requestsDao = requestsDao;
    }

    public List<String> getRequestsIds() {
        return requestsDao.getReqInfoIds();
    }

    public RequestInfo getRequestInfo(String reqId) {
        return requestsDao.getReqInfo(reqId);
    }

    public String saveFile(String reqId, @NotNull FileAdapter fileAdapter) {
        log.debug("Saving file content {} for request id {}", reqId, fileAdapter.getFilename());
        RequestInfo requestInfo = requestsDao.handleReqInfo(reqId);
        FileInfo fileInfo = FileInfo.builder().reqId(reqId).fileName(fileAdapter.getFilename()).fileBody(fileAdapter.getContent()).build();
        requestsDao.saveFile(fileInfo);
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
