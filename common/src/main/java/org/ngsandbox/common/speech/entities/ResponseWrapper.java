package org.ngsandbox.common.speech.entities;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ResponseWrapper<R> {
    private R response;
    private String msg;
    private ResponseStatus status;
}
