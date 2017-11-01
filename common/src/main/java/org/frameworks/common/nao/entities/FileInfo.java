package org.frameworks.common.nao.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileInfo {

    private String requestId;
    private String fileName;
    private byte[] fileBody;

    @Override
    public String toString() {
        return "FileInfo: {" +
                " requestId: " + requestId +
                ", fileName: " + fileName +
                " }";
    }

}
