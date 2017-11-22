package org.ngsandbox.common.nao.exceptions;

import org.ngsandbox.common.exceptions.NgError;

public class RequestNotFound extends NgError {
    public RequestNotFound(String message) {
        super(message);
    }

    public RequestNotFound(String message, Exception ex) {
        super(message, ex);
    }
}
