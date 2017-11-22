package org.ngsandbox.face.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class RegResponse {

    @JsonIgnore
    private String dto;

    private String errorDescription;
    private String status;
}
