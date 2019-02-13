package org.ngsandbox.common.speech.entities;

import lombok.*;


@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"response", "status"})
public class ResponseWrapper<R> {
    private R response;
    private String msg;
    private ResponseStatus status;

    private ResponseWrapper(R response, String msg, ResponseStatus status) {
        this.response = response;
        this.msg = msg;
        this.status = status;
    }

    public static <T> ResponseWrapper<T> fine(T value) {
        return new ResponseWrapper<>(value, "", ResponseStatus.OK);
    }

    public static <T> ResponseWrapper<T> fail(@NonNull ResponseStatus status, @NonNull String errorMessage) {
        return new ResponseWrapper<>(null, errorMessage, status);
    }
}
