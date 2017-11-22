package org.ngsandbox.common.face;

import lombok.NonNull;
import org.ngsandbox.common.file.FileAdapter;

public interface FaceService {
    ResponseWrapper<BaseResponse> register(@NonNull String login, @NonNull FileAdapter adapter);

    ResponseWrapper<BaseResponse> auth(@NonNull String login, @NonNull FileAdapter adapter);

    ResponseWrapper<BaseResponse> find(@NonNull FileAdapter adapter);
}
