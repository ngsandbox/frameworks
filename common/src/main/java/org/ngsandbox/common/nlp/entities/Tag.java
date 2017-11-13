package org.ngsandbox.common.nlp.entities;

import lombok.*;

import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@Builder
public class Tag implements Part {
    private String tag;
    private String label;
    @Singular
    private List<Part> children;

    @Override
    public String toString() {
        return "Tag{" +
                "tag='" +
                tag + '\'' + ", label='" + label + '\'' +
                ", children=[" +
                children.stream().map(c -> " \n " + c + ", ").collect(Collectors.toList()) +
                "]}";
    }
}
