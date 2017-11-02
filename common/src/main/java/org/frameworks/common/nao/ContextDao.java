package org.frameworks.common.nao;


import org.frameworks.common.nao.entities.ContextInfo;
import org.frameworks.common.nao.entities.FileInfo;

import java.util.List;

public interface ContextDao {

    List<String> getContextIds();

    ContextInfo getContext(String reqId);

    /**
     * Save request info and returns the previous value if it was exist
     * @param contextInfo request information
     * @return The previous request info
     */
    ContextInfo saveContext(ContextInfo contextInfo);

    ContextInfo handleContext(String contId);

    FileInfo getFile(String contId);

    FileInfo saveFile(FileInfo fileInfo);
}
