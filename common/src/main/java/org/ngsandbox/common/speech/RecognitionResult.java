package org.ngsandbox.common.speech;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;


@Builder
@Data
public class RecognitionResult {
    private int success;

    @Singular
    private List<Variant> variants;

}
