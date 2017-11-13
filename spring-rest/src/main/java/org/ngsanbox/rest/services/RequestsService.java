package org.ngsanbox.rest.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.ngsandbox.common.nao.ContextDao;
import org.ngsandbox.common.nao.entities.ContextInfo;
import org.ngsandbox.common.nao.entities.FileInfo;
import org.ngsandbox.common.nao.entities.ContextStatus;
import org.ngsandbox.common.nao.entities.QuestionInfo;
import org.ngsandbox.common.nlp.NlpService;
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

    private final ContextDao contextDao;
    private final NlpService nlpService;

    @Autowired
    public RequestsService(ContextDao contextDao, NlpService nlpService) {
        this.contextDao = contextDao;
        this.nlpService = nlpService;
    }

    public List<String> getRequestsIds() {
        return contextDao.getContextIds();
    }

    public ContextInfo getRequestInfo(String contId) {
        return contextDao.getContext(contId);
    }

    public ContextInfo saveFile(String contId, @NotNull FileAdapter fileAdapter) {
        log.debug("Saving file content {} for request id {}", contId, fileAdapter.getFilename());
        ContextInfo contextInfo = contextDao.handleContext(contId);
        FileInfo fileInfo = FileInfo.builder().reqId(contextInfo.getId()).fileName(fileAdapter.getFilename()).fileBody(fileAdapter.getContent()).build();
        contextDao.saveFile(fileInfo);
        contextInfo.setStatus(ContextStatus.ImageProcessing);
        log.debug("Changed request status {} ", contextInfo);
        contextDao.saveContext(contextInfo);
        return contextInfo;
    }

    private InputStream getFileStream(String reqId) {
        log.debug("Try to get file stream by id {}", reqId);
        FileInfo fileInfo = contextDao.getFile(reqId);
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

    public QuestionInfo answer2Question(String id, @NonNull String question) {
        ContextInfo contextInfo = contextDao.handleContext(id);
        QuestionInfo questionInfo = QuestionInfo.builder()
                .contextInfo(contextInfo)
                .question(question)
                .part(nlpService.processSentence(question))
                .answer("Hello!").build();
        contextInfo.setStatus(ContextStatus.Answer2Question);
        contextDao.saveContext(contextInfo);

        log.trace("Generated answer to the question {}", questionInfo);
        return questionInfo;
    }
}
