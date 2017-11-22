package org.ngsandbox.face.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class RegRequest implements Serializable {
    private String login;
    private String fotoName;
    private String desc;
    private String imageStr;
}
