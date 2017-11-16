package org.ngsandbox.common.face.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class FindRequest implements Serializable {
    private String imageStr;
}
