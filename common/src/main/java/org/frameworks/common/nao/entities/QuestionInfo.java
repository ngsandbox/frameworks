package org.frameworks.common.nao.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Builder
@Getter
@Setter
public class QuestionInfo implements Serializable {

    private ContextInfo contextInfo;
    private String question;
    private String answer;

    @Override
    public String toString() {
        return "QuestionInfo{" +
                "contextInfo=" + contextInfo +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}
