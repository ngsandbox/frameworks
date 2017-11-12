package org.frameworks.common.nlp.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
public class Word implements Part {
    private String word;
}
