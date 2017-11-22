package org.ngsandbox.face.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class AuthRequest implements Serializable {
    private String login;
    private String imageStr;
}
