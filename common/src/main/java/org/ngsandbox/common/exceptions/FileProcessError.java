package org.ngsandbox.common.exceptions;

import org.ngsandbox.common.nao.exceptions.NaoException;

public class FileProcessError extends NaoException {
    public FileProcessError(String message) {
        super(message);
    }

    public FileProcessError(String message, Exception ex) {
        super(message, ex);
    }
}
