package org.ngsandbox.common.file;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.ngsandbox.common.exceptions.FileProcessError;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class StreamFileAdapter implements FileAdapter {

    private final InputStream stream;
    private final String fileName;

    public StreamFileAdapter(@NonNull InputStream stream, @NonNull String fileName) {
        this.stream = stream;
        this.fileName = fileName;
    }

    @Override
    public String getFilename() {
        return fileName;
    }

    @Override
    public byte[] getContent() throws FileProcessError {
        try {
            return IOUtils.toByteArray(stream);
        } catch (IOException e) {
            log.error("Error to get bytes content of file {}", getFilename(), e);
            throw new FileProcessError("Error to parse content of file " + getFilename(), e);
        }
    }

    @Override
    public String getBase64() throws FileProcessError {
        return Base64.encodeBase64String(getContent());
    }

    @Override
    public InputStream getStream() throws FileProcessError {
        return stream;
    }
}
