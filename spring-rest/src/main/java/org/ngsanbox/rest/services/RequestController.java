package org.ngsanbox.rest.services;


import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.ngsanbox.rest.entities.RequestInfo;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
public class RequestController {

    private static final Map<String, RequestInfo> requests = new ConcurrentHashMap<>();

    static {
        Map<String, RequestInfo> values = IntStream.range(0, 10).mapToObj(i -> UUID.randomUUID().toString())
                .collect(Collectors.toMap(u -> u, p -> RequestInfo.builder().id(p).build()));
        requests.putAll(values);
    }


    @GetMapping("/requests")
    public List<String> getRequests() {
        return new ArrayList<>(requests.keySet());
    }

    @GetMapping("/requests/{id}")
    public RequestInfo getRequest(@PathVariable String id) {
        return requests.get(id);
    }

    @GetMapping(value = "/requests/{id}/download")
    public void getImageAsByteArray(@PathVariable String id, HttpServletResponse response) throws IOException {
        RequestInfo info = requests.get(id);
        if(info == null){
            response.setStatus(404);
            return;
        }

        InputStream in = new ByteArrayInputStream(info.getBody());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        IOUtils.copy(in, response.getOutputStream());
    }

    @PostMapping("/requests")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) throws Exception {
        RequestInfo requestInfo = RequestInfo.builder()
                .id(UUID.randomUUID().toString())
                .name(file.getOriginalFilename())
                .body(file.getBytes()).build();
        requests.put(requestInfo.getId(), requestInfo);
        return requestInfo.getId();
    }
}
