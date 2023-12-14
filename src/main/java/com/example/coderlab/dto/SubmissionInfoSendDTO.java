package com.example.coderlab.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SubmissionInfoSendDTO {
    private long assignment_id;
    private String sourceCode;
    private String language;

    private List<TestCaseListDTO> testCaseListDTOS;


    @Override
    public String toString() {
        return "SubmissionInfoSendDTO{" +
                "assignment_id=" + assignment_id +
                ", sourceCode='" + sourceCode + '\'' +
                ", language='" + language + '\'' +
                ", testCaseListDTOS=" + testCaseListDTOS +
                '}';
    }
}
