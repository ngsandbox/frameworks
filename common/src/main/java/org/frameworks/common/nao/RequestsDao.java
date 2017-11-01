package org.frameworks.common.nao;


import org.frameworks.common.nao.entities.FileInfo;
import org.frameworks.common.nao.entities.RequestInfo;

import java.util.List;

public interface RequestsDao {

    RequestInfo getReqInfo(String id);

    List<FileInfo> getFiles(String requestId);

}
