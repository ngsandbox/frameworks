package org.ngsandbox.common.nao.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class FaceInfo implements Serializable {

    private final ContextInfo contextInfo;

    private final String login;

    private final String msg;


    @Override
    public String toString() {
        return "FaceInfo{" +
                "contextInfo=" + contextInfo +
                ", login='" + login + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
