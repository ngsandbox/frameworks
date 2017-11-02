package org.frameworks.hazelcast;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.frameworks.common.nao.ContextDao;
import org.frameworks.common.nao.entities.ContextInfo;
import org.frameworks.common.nao.entities.FileInfo;
import org.frameworks.common.nao.entities.ContextStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
public class HazelContextDaoImpl implements ContextDao {

    private final HazelService hazelService;

    public HazelContextDaoImpl(HazelService hazelService) {
        this.hazelService = hazelService;
    }

    @Override
    public ContextInfo getContext(@NonNull String contId) {
        return hazelService.getContextsMap().get(contId);
    }


    @Override
    public List<String> getContextIds() {
        return new ArrayList<>(hazelService.getContextsMap().keySet());
    }

    @Override
    public ContextInfo saveContext(@NonNull ContextInfo contextInfo) {
        return hazelService.getContextsMap().put(contextInfo.getId(), contextInfo);
    }

    @Override
    public ContextInfo handleContext(String contId) {
        if (contId == null || contId.equals("")) {
            contId = UUID.randomUUID().toString();
            log.trace("Create new request info for contId {}", contId);
            ContextInfo contextInfo = ContextInfo.builder()
                    .id(contId)
                    .status(ContextStatus.Initialized).build();
            saveContext(contextInfo);
            log.trace("Request info for contId {} created. {}", contId, contextInfo);
            return contextInfo;
        }

        log.trace("Receive request info for contId {}", contId);
        return getContext(contId);
    }


    @Override
    public FileInfo getFile(@NonNull String contId) {
        return hazelService.getFilesMap().get(contId);
    }

    @Override
    public FileInfo saveFile(@NonNull FileInfo fileInfo) {
        FileInfo prev = hazelService.getFilesMap().put(fileInfo.getReqId(), fileInfo);
        log.trace("Saved new file {}. Previous was: {}", fileInfo, prev);
        return prev;
    }
}
