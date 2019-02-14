package org.ngsandbox.elastic.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Builder
@Data
public class Quote implements ElasticJson {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @Builder.Default
    private String id = UUID.randomUUID().toString();
    private String symbol;
    private String tier;

    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
    private Date created = new Date();

    private List<Price> prices;


    @Override
    public String takeId() {
        return getId();
    }

    @Override
    public String takeJson() {
        try {
            return MAPPER.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }
}
