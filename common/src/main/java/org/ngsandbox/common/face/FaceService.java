package org.ngsandbox.common.face;

import lombok.NonNull;
import org.ngsandbox.common.face.dto.AuthResponse;
import org.ngsandbox.common.face.dto.FindResponse;
import org.ngsandbox.common.face.dto.RegResponse;
import org.ngsandbox.common.file.FileAdapter;

public interface FaceService {
    ResponseWrapper<RegResponse> register(@NonNull String login, @NonNull FileAdapter adapter);

    ResponseWrapper<AuthResponse> auth(@NonNull String login, @NonNull FileAdapter adapter);

    ResponseWrapper<FindResponse> find(@NonNull FileAdapter adapter);
}
