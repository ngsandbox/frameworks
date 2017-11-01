package org.ngsanbox.rest.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class RequestInfo {
    private String id;
    private String fileName;
    private RequestStatus status;
    private byte[] fileBody;

    @Override
    public String toString() {
        return "RequestInfo: {" +
                " id: " + id +
                ", status: " + status +
                ", fileName: " + fileName +
                " }";
    }
}

