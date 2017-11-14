package org.ngsandbox.common.face;

import lombok.Data;

@Data
public class RegResponse {

    private RegResponse dto;
    private String errorDescription;
    private String status;


    /*
    {"dto":
    {"login":"IvanovNS",
    "imageStr":" строка из байтов ",
    "fotoName":"",
    "desc":"IvanovNS",
    "errorCode":null,
    "errorDesc":null},
    "errorDescription":"Регистрация: успешно",
    "status":"SUCCESS"}
    * */
}
