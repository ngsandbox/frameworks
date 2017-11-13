package org.ngsandbox.common.nlp.entities;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;


@Data
@Builder
public class Tag implements Part {
    private String tag;
    private String label;
    @Singular
    private List<Part> children;
}
