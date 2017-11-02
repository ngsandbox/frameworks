package org.ngsanbox.rest.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.frameworks.common.nao.RequestsDao;
import org.frameworks.common.nao.entities.FileInfo;
import org.frameworks.common.nao.entities.RequestInfo;
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
import java.util.List;

@Slf4j
@Service
public class RequestsService {

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

    public FileInfo saveFile(String reqId, @NotNull FileAdapter fileAdapter) {
        log.debug("Saving file content {} for request id {}", reqId, fileAdapter.getFilename());
        RequestInfo requestInfo = requestsDao.handleReqInfo(reqId);
        FileInfo fileInfo = FileInfo.builder().reqId(requestInfo.getId()).fileName(fileAdapter.getFilename()).fileBody(fileAdapter.getContent()).build();
        requestsDao.saveFile(fileInfo);
        return fileInfo;
    }

    private InputStream getFileStream(String reqId) {
        log.debug("Try to get file stream by id {}", reqId);
        FileInfo fileInfo = requestsDao.getLastFile(reqId);
        if (fileInfo == null) {
            throw new RequestNotFound("File not found for request id : " + reqId);
        }

        return new ByteArrayInputStream(fileInfo.getFileBody());
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
