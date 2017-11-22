package org.ngsandbox.speechkit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Variant {
    private double confidence;
    private String value;
}
