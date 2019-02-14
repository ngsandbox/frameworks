package org.ngsandbox.elastic.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Data
public class QuoteIndex implements ElasticJson {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
    private Date datetime;


    @JsonIgnore
    @Delegate(excludes = QuoteExclusion.class)
    private final Quote quote;

    @JsonIgnore
    @Delegate
    private final Price price;


    private interface QuoteExclusion {
        Date getCreated();

        List<Price> getPrices();
    }

    public QuoteIndex(@NonNull Quote quote, @NonNull Price price) {
        this.quote = quote;
        this.datetime = quote.getCreated();
        this.price = price;
    }


    @Override
    public String takeId() {
        return UUID.nameUUIDFromBytes((quote.getId() + price.getVolume()).getBytes()).toString();
    }

    @Override
    public String takeJson() {
        try {
            return MAPPER.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            log.error("Error convert to json object {}", this, e);
        }

        return null;
    }
}
