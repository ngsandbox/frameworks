package org.frameworks.hazelcast;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.frameworks.common.nao.RequestsDao;
import org.frameworks.common.nao.entities.FileInfo;
import org.frameworks.common.nao.entities.RequestInfo;
import org.frameworks.common.nao.entities.RequestStatus;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Slf4j
public class HazelRequestsDaoImpl implements RequestsDao {

    private final HazelService hazelService;

    public HazelRequestsDaoImpl(HazelService hazelService) {
        this.hazelService = hazelService;
    }

    @Override
    public RequestInfo getReqInfo(@NonNull String reqId) {
        return hazelService.getRequestsMap().get(reqId);
    }

    @Override
    public RequestInfo saveReqInfo(@NonNull RequestInfo reqInfo) {
        return hazelService.getRequestsMap().put(reqInfo.getId(), reqInfo);
    }

    @Override
    public RequestInfo handleReqInfo(String reqId) {
        if (reqId == null) {
            reqId = UUID.randomUUID().toString();
            log.trace("Create new request info for reqId {}", reqId);
            RequestInfo requestInfo = RequestInfo.builder()
                    .id(reqId)
                    .status(RequestStatus.Initialized).build();
            saveReqInfo(requestInfo);
            log.trace("Request info for reqId {} created. {}", reqId, requestInfo);
            return requestInfo;
        }

        log.trace("Receive request info for reqId {}", reqId);
        return getReqInfo(reqId);
    }


    @Override
    public List<FileInfo> getFiles(@NonNull String reqId) {
        return hazelService.getFilesMap().get(reqId);
    }

    @Override
    public void saveFile(@NonNull FileInfo fileInfo) {
        List<FileInfo> files = new LinkedList<>();
        files = hazelService.getFilesMap().putIfAbsent(fileInfo.getReqId(),files);

        hazelService.getFilesMap().put(fileInfo.getReqId(),files);
    }

    @Override
    public List<String> getReqInfoIds() {
        return new ArrayList<>(hazelService.getRequestsMap().keySet());
    }
}
