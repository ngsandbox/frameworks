package org.ngsanbox.rest.adapters;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.ngsanbox.rest.exceptions.FileProcessError;
import org.ngsandbox.common.file.FileAdapter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
public class MultipartFileAdapter implements FileAdapter {

    private final MultipartFile file;

    public MultipartFileAdapter(@NonNull MultipartFile file) {
        this.file = file;
    }

    @Override
    public String getFilename() {
        return file.getOriginalFilename();
    }

    @Override
    public byte[] getContent() throws FileProcessError {
        try {
            return file.getBytes();
        } catch (IOException e) {
            log.error("Error to get bytes content of file {}", getFilename(), e);
            throw new FileProcessError("Error to parse content of file " + getFilename(), e);
        }
    }

    @Override
    public String getBase64() throws FileProcessError {
        return Base64.encodeBase64String(getContent());
    }
}
