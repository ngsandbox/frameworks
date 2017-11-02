package org.ngsanbox.rest.controllers;


import lombok.extern.slf4j.Slf4j;
import org.frameworks.common.nao.entities.ContextInfo;
import org.frameworks.common.nao.entities.QuestionInfo;
import org.ngsanbox.rest.adapters.Base64FileAdapter;
import org.ngsanbox.rest.adapters.MultipartFileAdapter;
import org.ngsanbox.rest.services.RequestsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@RestController
public class RequestController {


    private final RequestsService requestsService;

    @Autowired
    public RequestController(RequestsService requestsService) {
        this.requestsService = requestsService;
    }


    @GetMapping("/requests")
    public List<String> getRequests() {
        log.trace("Get request identifiers");
        return requestsService.getRequestsIds();
    }

    @GetMapping("/requests/{id}")
    public ContextInfo getContext(@PathVariable String id) {
        log.trace("Get request info by id {}", id);
        return requestsService.getRequestInfo(id);
    }

    @GetMapping(value = "/requests/{id}/download")
    public void downloadImage(@PathVariable String id, HttpServletResponse response) {
        log.trace("Get image by request info id {}", id);
        requestsService.outputImageStream(id, response);
    }

    @PostMapping("/requests/file")
    public ContextInfo handleFileUpload(@RequestParam(required = false) String id, @RequestParam("file") MultipartFile file) {
        log.trace("Save file content for id {}", id);
        return requestsService.saveFile(id, new MultipartFileAdapter(file));
    }

    @PostMapping("/requests/base64")
    public ContextInfo handleBase64Upload(@RequestParam(required = false) String id,
                                          @RequestParam("fileName") String fileName,
                                          @RequestParam("fileContent") String fileContent) {
        log.trace("Save base64 file content of fileName {} for request info id {}", fileName, id);
        return requestsService.saveFile(id, new Base64FileAdapter(fileName, fileContent));
    }

    @PostMapping("/requests/ask")
    public QuestionInfo answer2Question(@RequestParam(required = false) String id, @RequestParam("question") String question) {
        log.trace("For request {} try to anser to the question  {}", id, question);
        return requestsService.answer2Question(id, question);
    }
}
