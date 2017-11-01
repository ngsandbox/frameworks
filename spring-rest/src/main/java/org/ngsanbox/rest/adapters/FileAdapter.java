package org.ngsanbox.rest.adapters;

import org.ngsanbox.rest.exceptions.FileProcessError;

public interface FileAdapter {
    String getFilename();

    byte[] getContent() throws FileProcessError;
}
