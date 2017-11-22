package org.ngsandbox.speechkit;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.ngsandbox.common.exceptions.HttpError;
import org.ngsandbox.common.file.FileAdapter;
import org.ngsandbox.common.speech.SpeechService;
import org.ngsandbox.common.speech.entities.BaseResponse;
import org.ngsandbox.common.speech.entities.ResponseStatus;
import org.ngsandbox.common.speech.entities.ResponseWrapper;
import org.ngsandbox.speechkit.dto.RecognitionResult;
import org.ngsandbox.speechkit.dto.Variant;

import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

@Slf4j
public class SpeechKitServiceImpl implements SpeechService {

    private final SpeechKitService service;

    public SpeechKitServiceImpl(@NonNull Properties properties) {
        String host = properties.getProperty("host");
        String hostUrl = host == null ? "https://asr.yandex.net" : host;
        String apiKey = properties.getProperty("apiKey");
        Objects.requireNonNull(apiKey, "Api key for SpeechKit is not specified");
        service = new SpeechKitService(hostUrl, apiKey);
    }


    @Override
    public ResponseWrapper<BaseResponse> parseVoice(@NonNull FileAdapter adapter) {
        String errMsg;
        try (InputStream result = service.post(adapter.getStream())) {
            ResponseConverter responseConverter = new ResponseConverter(result);
            RecognitionResult recognitionResult = responseConverter.getRecognitionResult();
            log.debug("Result recognition {}", recognitionResult);
            if (!recognitionResult.getVariants().isEmpty()) {
                Variant variant = recognitionResult.getVariants().get(0);
                return new ResponseWrapper<>(new BaseResponse(variant.getValue()), "", ResponseStatus.OK);
            }

            log.warn("No variants was found for file {} ", adapter.getFilename());
            return new ResponseWrapper<>(null, "The response from SpeechKit is empty", ResponseStatus.RESPONSE_ERROR);
        } catch (HttpError e) {
            errMsg = "Error to call SpeechKit server to process the file " + adapter.getFilename();
            log.error(errMsg, e);
        } catch (Exception e) {
            errMsg = "Error to process result from SpeechKit server for file " + adapter.getFilename();
            log.error(errMsg, e);
        }

        return new ResponseWrapper<>(null, errMsg, ResponseStatus.SERVER_ERROR);
    }
}
