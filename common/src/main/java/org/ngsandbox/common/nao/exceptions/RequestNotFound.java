package org.ngsandbox.common.nao.exceptions;

public class RequestNotFound extends NaoException {
    public RequestNotFound(String message) {
        super(message);
    }

    public RequestNotFound(String message, Exception ex) {
        super(message, ex);
    }
}
