package org.ngsandbox.common.face.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class AuthDtoResponse {

    /*
    {"dto":{"login":null,"imageStr":"строка из байтов ","trackId":null,"errorCode":null, "errorDesc":null,"time":null,"images":null}
    * */
    @JsonIgnore
    private String imageStr;

    @JsonIgnore
    private String images;

    private String login;
    private String trackId;
    private String errorCode;
    private String errorDesc;
    private String time;
}
