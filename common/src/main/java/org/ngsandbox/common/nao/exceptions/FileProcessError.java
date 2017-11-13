package org.ngsandbox.common.nao.exceptions;

public class FileProcessError extends NaoException {
    public FileProcessError(String message) {
        super(message);
    }

    public FileProcessError(String message, Exception ex) {
        super(message, ex);
    }
}
