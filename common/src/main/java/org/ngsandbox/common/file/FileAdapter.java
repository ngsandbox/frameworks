package org.ngsandbox.common.file;

import org.ngsandbox.common.exceptions.FileProcessError;

import java.io.InputStream;

public interface FileAdapter {
    String getFilename();

    byte[] getContent() throws FileProcessError;

    String getBase64() throws FileProcessError;

    InputStream getStream() throws FileProcessError;
}
