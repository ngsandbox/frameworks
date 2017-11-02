package org.frameworks.common.nao.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Builder
@Getter
@Setter
public class RequestInfo implements Serializable {
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

