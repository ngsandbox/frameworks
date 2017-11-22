package org.ngsandbox.common.exceptions;

public class NgError extends RuntimeException {

    public NgError(String message) {
        super(message);
    }

    public NgError(String message, Throwable ex) {
        super(message, ex);
    }
}
