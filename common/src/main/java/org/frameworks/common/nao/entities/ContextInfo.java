package org.frameworks.common.nao.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Builder
@Getter
@Setter
public class ContextInfo implements Serializable {
    private String id;
    private ContextStatus status;

    @Override
    public String toString() {
        return "ContextInfo: {" +
                " id: " + id +
                ", status: " + status +
                " }";
    }
}

