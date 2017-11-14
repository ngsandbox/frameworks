package org.ngsandbox.common.file;

import org.ngsandbox.common.exceptions.FileProcessError;

public interface FileAdapter {
    String getFilename();

    byte[] getContent() throws FileProcessError;
}
