package org.ngsandbox.common.nao.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.ngsandbox.common.nlp.entities.Part;

import java.io.Serializable;

@Builder
@Getter
@Setter
@ToString
public class QuestionInfo implements Serializable {

    private ContextInfo contextInfo;
    private String question;
    private String answer;
    private Part part;
}
