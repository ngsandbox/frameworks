package org.ngsandbox.face;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.ngsandbox.common.exceptions.HttpError;
import org.ngsandbox.common.face.FaceService;
import org.ngsandbox.common.face.ResponseStatus;
import org.ngsandbox.common.face.ResponseWrapper;
import org.ngsandbox.common.face.dto.*;
import org.ngsandbox.common.file.FileAdapter;

@Slf4j
public class RemoteFaceServiceImpl implements FaceService {

    private final String hostName;

    public RemoteFaceServiceImpl(@NonNull String hostName) {
        this.hostName = hostName;
    }

    @Override
    public ResponseWrapper<RegResponse> register(@NonNull String login, @NonNull FileAdapter adapter) {
        try {
            RemoteFaceService<RegResponse> serv = new RemoteFaceService<>(hostName, RegResponse.class);
            RegRequest req = RegRequest.builder()
                    .fotoName("")
                    .desc("")
                    .imageStr(adapter.getBase64())
                    .login(login)
                    .build();
            RegResponse regResponse = serv.post("/regist", req);
            log.debug("Register response {}", regResponse);
            return new ResponseWrapper<>(regResponse, ResponseStatus.OK);
        } catch (HttpError ex) {
            return new ResponseWrapper<>(null, ResponseStatus.ERROR);
        }
    }

    @Override
    public ResponseWrapper<AuthResponse> auth(@NonNull String login, @NonNull FileAdapter adapter) {
        try {
            RemoteFaceService<AuthResponse> serv = new RemoteFaceService<>(hostName, AuthResponse.class);
            AuthRequest req = AuthRequest.builder()
                    .imageStr(adapter.getBase64())
                    .login(login)
                    .build();
            AuthResponse authResp = serv.post("/auth", req);
            log.debug("Auth response {}", authResp);
            return new ResponseWrapper<>(authResp, ResponseStatus.OK);
        } catch (HttpError ex) {
            return new ResponseWrapper<>(null, ResponseStatus.ERROR);
        }
    }

    @Override
    public ResponseWrapper<FindResponse> find(@NonNull FileAdapter adapter) {
        try {
            RemoteFaceService<FindResponse> serv = new RemoteFaceService<>(hostName, FindResponse.class);
            FindRequest req = FindRequest.builder()
                    .imageStr(adapter.getBase64())
                    .build();
            FindResponse findResp = serv.post("/find", req);
            log.debug("Find response {}", findResp);
            return new ResponseWrapper<>(findResp, ResponseStatus.OK);
        } catch (HttpError ex) {
            return new ResponseWrapper<>(null, ResponseStatus.ERROR);
        }
    }


}