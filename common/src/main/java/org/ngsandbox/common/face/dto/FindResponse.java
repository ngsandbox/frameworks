package org.ngsandbox.common.face.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FindResponse {

    @JsonProperty("dto")
    private String login;

    private String errorDescription;
    private String status;
}
