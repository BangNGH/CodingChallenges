package com.example.coderlab.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SubmissionKitInfoSendDTO {
    private List<String> submissions_id_ofAssignment;
    private List<String> submissions_id_ofQuiz;
    private String assignment_kit_id;

    @Override
    public String toString() {
        return "SubmissionKitInfoSendDTO{" +
                "submissions_id_ofAssignment=" + submissions_id_ofAssignment +
                ", submissions_id_ofQuiz=" + submissions_id_ofQuiz +
                '}';
    }
}
