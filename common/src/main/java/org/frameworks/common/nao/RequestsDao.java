package org.frameworks.common.nao;


import org.frameworks.common.nao.entities.FileInfo;
import org.frameworks.common.nao.entities.RequestInfo;

import java.util.List;

public interface RequestsDao {

    RequestInfo getReqInfo(String reqId);

    /**
     * Save request info and returns the previous value if it was exist
     * @param reqInfo request information
     * @return The previous request info
     */
    RequestInfo saveReqInfo(RequestInfo reqInfo);

    RequestInfo handleReqInfo(String reqId);

    List<FileInfo> getFiles(String reqId);

    void saveFile(FileInfo fileInfo);

    List<String> getReqInfoIds();

    FileInfo getLastFile(String reqId);
}
