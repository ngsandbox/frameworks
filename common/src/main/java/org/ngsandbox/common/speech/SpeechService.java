package org.ngsandbox.common.speech;

import org.ngsandbox.common.file.FileAdapter;
import org.ngsandbox.common.speech.entities.BaseResponse;
import org.ngsandbox.common.speech.entities.ResponseWrapper;

public interface SpeechService {

    ResponseWrapper<BaseResponse> parseVoice(FileAdapter adapter);
}
