package org.ngsandbox.common.file;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.ngsandbox.common.exceptions.FileProcessError;

@Slf4j
public class Base64FileAdapter implements FileAdapter {

    private final String fileName;
    private final String fileContent;

    public Base64FileAdapter(@NonNull String fileName, @NonNull String fileContent) {
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
