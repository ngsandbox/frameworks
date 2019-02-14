package org.ngsandbox.elastic.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Builder
@Data
public class Quote {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Builder.Default
    private String id = UUID.randomUUID().toString();

    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
    private Date created = new Date();

    @NonNull
    private String symbol;
    @NonNull
    private String tier;


    private List<Price> prices;
}
