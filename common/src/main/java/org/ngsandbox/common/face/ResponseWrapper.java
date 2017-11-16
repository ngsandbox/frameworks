package org.ngsandbox.common.face;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ResponseWrapper<R> {
    private R response;
    private ResponseStatus status;
}
