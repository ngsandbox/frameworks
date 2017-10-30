package org.ngsandbox.elastic.entities;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Price {
    private int volume;
    private double bid;
    private double offer;
}
