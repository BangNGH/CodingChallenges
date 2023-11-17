package com.example.coderlab.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SubmissionKitInfoSendDTO {
    private List<String> submissions_id;
    private String assignment_kit_id;

    @Override
    public String toString() {
        return "SubmissionKitInfoSendDTO{" +
                "submissions_id=" + submissions_id +
                ", assignment_kit_id=" + assignment_kit_id +
                '}';
    }
}
