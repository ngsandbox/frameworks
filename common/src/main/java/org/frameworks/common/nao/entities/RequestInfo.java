package org.frameworks.common.nao.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class RequestInfo {
    private String id;
    private RequestStatus status;

    @Override
    public String toString() {
        return "RequestInfo: {" +
                " id: " + id +
                ", status: " + status +
                " }";
    }
}

