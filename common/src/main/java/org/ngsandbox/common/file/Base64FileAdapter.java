package org.ngsandbox.common.file;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.ngsandbox.common.exceptions.FileProcessError;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Slf4j
public class Base64FileAdapter implements FileAdapter {

    private final String fileName;
    private final String fileContent;

    public Base64FileAdapter(@NonNull String fileName, @NonNull String base64Content) {
        this.fileName = fileName;
        this.fileContent = base64Content;
    }

    public Base64FileAdapter(@NonNull String fileName, @NonNull InputStream stream) throws IOException {
        this.fileName = fileName;
        byte[] bytes = IOUtils.toByteArray(stream);
        this.fileContent = Base64.encodeBase64String(bytes);
    }

    @Override
    public String getFilename() {
        return fileName;
    }

    @Override
    public byte[] getContent() throws FileProcessError {
        return fileContent.getBytes();
    }

    @Override
    public String getBase64() throws FileProcessError {
        return fileContent;
    }
}
