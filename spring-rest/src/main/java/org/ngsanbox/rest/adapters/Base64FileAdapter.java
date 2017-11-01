package org.ngsanbox.rest.adapters;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.ngsanbox.rest.exceptions.FileProcessError;

import javax.validation.constraints.NotNull;

@Slf4j
public class Base64FileAdapter implements FileAdapter {

    private final String fileName;
    private final String fileContent;

    public Base64FileAdapter(@NonNull String fileName, @NotNull String fileContent) {
        this.fileName = fileName;
        this.fileContent = fileContent;
    }

    @Override
    public String getFilename() {
        return fileName;
    }

    @Override
    public byte[] getContent() throws FileProcessError {
        return Base64.decodeBase64(fileContent);
    }
}
