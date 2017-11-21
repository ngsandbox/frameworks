package org.ngsandbox.common.face;

public enum ResponseStatus {
    OK, RESPONSE_ERROR, SERVER_ERROR;

    public boolean isSuccess() {
        return !this.equals(RESPONSE_ERROR) && !this.equals(SERVER_ERROR);
    }
}
