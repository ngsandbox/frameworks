package org.frameworks.common.nao.exceptions;

public class NaoException extends RuntimeException {

    public NaoException(String message) {
        super(message);
    }

    public NaoException(String message, Exception ex) {
        super(message, ex);
    }
}
