package org.ngsandbox.common.exceptions;

public class FileProcessError extends NgError {
    public FileProcessError(String message) {
        super(message);
    }

    public FileProcessError(String message, Exception ex) {
        super(message, ex);
    }
}
