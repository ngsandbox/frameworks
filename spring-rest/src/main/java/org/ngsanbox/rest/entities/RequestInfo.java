package org.ngsanbox.rest.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter @Setter
public class RequestInfo {
    private String id;
    private String name;
    private byte[] body;
}
