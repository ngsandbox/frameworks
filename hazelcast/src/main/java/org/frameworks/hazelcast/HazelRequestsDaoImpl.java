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
    public LinkedList<FileInfo> getFiles(@NonNull String reqId) {
        List<FileInfo> files = hazelService.getFilesMap().get(reqId);
        if (files == null) {
            return new LinkedList<>();
        }

        return new LinkedList<>(files);
    }

    @Override
    public void saveFile(@NonNull FileInfo fileInfo) {
        LinkedList<FileInfo> files = getFiles(fileInfo.getReqId());
        files.addLast(fileInfo);
        hazelService.getFilesMap().put(fileInfo.getReqId(), files);
    }

    @Override
    public List<String> getReqInfoIds() {
        return new ArrayList<>(hazelService.getRequestsMap().keySet());
    }

    @Override
    public FileInfo getLastFile(@NonNull String reqId) {
        LinkedList<FileInfo> files = getFiles(reqId);
        return files.isEmpty() ? null : files.getLast();
    }
}
