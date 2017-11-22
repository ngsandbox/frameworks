package org.ngsandbox.face;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.ngsandbox.common.exceptions.HttpError;
import org.ngsandbox.common.face.BaseResponse;
import org.ngsandbox.common.face.FaceService;
import org.ngsandbox.common.face.ResponseStatus;
import org.ngsandbox.common.face.ResponseWrapper;
import org.ngsandbox.common.file.FileAdapter;
import org.ngsandbox.face.dto.*;

@Slf4j
public class RemoteFaceServiceImpl implements FaceService {

    private final String hostName;

    public RemoteFaceServiceImpl(@NonNull String hostName) {
        this.hostName = hostName;
    }

    @Override
    public ResponseWrapper<BaseResponse> register(@NonNull String login, @NonNull FileAdapter adapter) {
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
            if (regResponse != null) {
                ResponseStatus status = regResponse.getStatus().equalsIgnoreCase("success") ?
                        ResponseStatus.OK :
                        ResponseStatus.RESPONSE_ERROR;
                return new ResponseWrapper<>(new BaseResponse(login), "", status);
            }
            return new ResponseWrapper<>(null, "The response from server is empty", ResponseStatus.RESPONSE_ERROR);
        } catch (HttpError ex) {
            return new ResponseWrapper<>(null, ex.getMessage(), ResponseStatus.SERVER_ERROR);
        }
    }

    @Override
    public ResponseWrapper<BaseResponse> auth(@NonNull String login, @NonNull FileAdapter adapter) {
        try {
            RemoteFaceService<AuthResponse> serv = new RemoteFaceService<>(hostName, AuthResponse.class);
            AuthRequest req = AuthRequest.builder()
                    .imageStr(adapter.getBase64())
                    .login(login)
                    .build();
            AuthResponse authResp = serv.post("/auth", req);
            log.debug("Auth response {}", authResp);
            if (authResp != null) {
                ResponseStatus status = authResp.getStatus().equalsIgnoreCase("success") ?
                        ResponseStatus.OK :
                        ResponseStatus.RESPONSE_ERROR;
                return new ResponseWrapper<>(new BaseResponse(login), authResp.getErrorDescription(), status);
            }

            return new ResponseWrapper<>(null, "The response from server is empty", ResponseStatus.RESPONSE_ERROR);
        } catch (HttpError ex) {
            return new ResponseWrapper<>(null, ex.getMessage(), ResponseStatus.SERVER_ERROR);
        }
    }

    @Override
    public ResponseWrapper<BaseResponse> find(@NonNull FileAdapter adapter) {
        try {
            RemoteFaceService<FindResponse> serv = new RemoteFaceService<>(hostName, FindResponse.class);
            FindRequest req = FindRequest.builder()
                    .imageStr(adapter.getBase64())
                    .build();
            FindResponse findResp = serv.post("/find", req);
            log.debug("Find response {}", findResp);
            if (findResp != null) {
                ResponseStatus status = findResp.getStatus().equalsIgnoreCase("success") ?
                        ResponseStatus.OK :
                        ResponseStatus.RESPONSE_ERROR;
                return new ResponseWrapper<>(new BaseResponse(findResp.getLogin()), findResp.getErrorDescription(), status);
            }

            return new ResponseWrapper<>(null, "The response from server is empty", ResponseStatus.RESPONSE_ERROR);

        } catch (HttpError ex) {
            return new ResponseWrapper<>(null, ex.getMessage(), ResponseStatus.SERVER_ERROR);
        }
    }


}
