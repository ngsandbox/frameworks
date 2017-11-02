package org.frameworks.common.nao.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class FileInfo {

    private String reqId;
    private String fileName;
    private byte[] fileBody;

    @Override
    public String toString() {
        return "FileInfo: {" +
                " reqId: " + reqId +
                ", fileName: " + fileName +
                " }";
    }

}
