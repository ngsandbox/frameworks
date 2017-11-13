package org.ngsandbox.common.nao.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Builder
@Getter
@Setter
@ToString
public class ContextInfo implements Serializable {
    private String id;
    private ContextStatus status;
}

